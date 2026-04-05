use std::path::PathBuf;
use std::net::TcpStream;
use std::process::{Child, Command, Stdio};
use std::sync::Mutex;
use tauri::Manager;

struct BackendState {
    child: Mutex<Option<Child>>,
    startup_error: Mutex<Option<String>>,
}

#[tauri::command]
fn get_startup_error(state: tauri::State<'_, BackendState>) -> Option<String> {
    state.startup_error.lock().ok().and_then(|v| v.clone())
}

#[tauri::command]
fn is_backend_ready_cmd() -> bool {
    is_backend_ready()
}

#[tauri::command]
fn open_logs_dir() -> Result<(), String> {
    let logs_dir = PathBuf::from(std::env::var("LOCALAPPDATA").unwrap_or_else(|_| ".".to_string()))
        .join("FriendHollow")
        .join("logs");
    std::fs::create_dir_all(&logs_dir).map_err(|e| format!("无法创建日志目录: {e}"))?;
    Command::new("explorer")
        .arg(logs_dir)
        .spawn()
        .map_err(|e| format!("打开日志目录失败: {e}"))?;
    Ok(())
}

#[tauri::command]
fn open_runtime_dir() -> Result<(), String> {
    let runtime_dir = PathBuf::from(std::env::var("LOCALAPPDATA").unwrap_or_else(|_| ".".to_string()))
        .join("FriendHollow")
        .join("runtime");
    std::fs::create_dir_all(&runtime_dir).map_err(|e| format!("无法创建运行目录: {e}"))?;
    Command::new("explorer")
        .arg(runtime_dir)
        .spawn()
        .map_err(|e| format!("打开运行目录失败: {e}"))?;
    Ok(())
}

#[tauri::command]
fn retry_backend_start(app: tauri::AppHandle, state: tauri::State<'_, BackendState>) -> Result<(), String> {
    if is_backend_ready() {
        if let Ok(mut err) = state.startup_error.lock() {
            *err = None;
        }
        return Ok(());
    }
    if let Ok(mut guard) = state.child.lock() {
        if let Some(child) = guard.as_mut() {
            let _ = child.kill();
        }
        *guard = None;
    }
    match start_backend(&app) {
        Ok(child) => {
            if let Ok(mut guard) = state.child.lock() {
                *guard = Some(child);
            }
            if let Ok(mut err) = state.startup_error.lock() {
                *err = None;
            }
            Ok(())
        }
        Err(e) => {
            if let Ok(mut err) = state.startup_error.lock() {
                *err = Some(e.clone());
            }
            Err(e)
        }
    }
}

fn is_backend_ready() -> bool {
    TcpStream::connect("127.0.0.1:8080").is_ok()
}

fn resolve_java_cmd() -> String {
    if let Ok(java_home) = std::env::var("JAVA_HOME") {
        let java_exe = PathBuf::from(java_home).join("bin").join("java.exe");
        if java_exe.exists() {
            return java_exe.to_string_lossy().to_string();
        }
    }
    "java".to_string()
}

fn start_backend(app: &tauri::AppHandle) -> Result<Child, String> {
    let resource_dir = app.path().resource_dir().ok();
    let exe_dir = std::env::current_exe()
        .ok()
        .and_then(|p| p.parent().map(|x| x.to_path_buf()));
    let cwd = std::env::current_dir().ok();
    let jar_name = "hollow-1.0.0.jar";
    let jar_candidates = vec![
        resource_dir.clone().map(|d| d.join(jar_name)),
        resource_dir.clone().map(|d| d.join("resources").join(jar_name)),
        exe_dir.clone().map(|d| d.join(jar_name)),
        exe_dir.clone().map(|d| d.join("resources").join(jar_name)),
        exe_dir
            .clone()
            .map(|d| d.join("..").join("resources").join(jar_name)),
        exe_dir
            .clone()
            .map(|d| d.join("..").join("..").join("target").join(jar_name)),
        cwd.clone().map(|d| d.join("target").join(jar_name)),
        cwd.clone().map(|d| d.join(jar_name)),
    ];
    let jar_path = jar_candidates.into_iter().flatten().find(|p| p.exists());

    let local_app_data = std::env::var("LOCALAPPDATA").unwrap_or_else(|_| ".".to_string());
    let upload_dir = PathBuf::from(local_app_data)
        .join("FriendHollow")
        .join("uploads")
        .join("starlight");
    let app_home = PathBuf::from(std::env::var("LOCALAPPDATA").unwrap_or_else(|_| ".".to_string()))
        .join("FriendHollow");
    let logs_dir = app_home.join("logs");
    let runtime_dir = app_home.join("runtime");
    let runtime_jar = runtime_dir.join(jar_name);

    std::fs::create_dir_all(&upload_dir)
        .map_err(|e| format!("创建上传目录失败: {e}"))?;
    std::fs::create_dir_all(&logs_dir)
        .map_err(|e| format!("创建日志目录失败: {e}"))?;
    std::fs::create_dir_all(&runtime_dir)
        .map_err(|e| format!("创建运行目录失败: {e}"))?;

    if let Some(found) = jar_path {
        std::fs::copy(found, &runtime_jar)
            .map_err(|e| format!("复制后端包到运行目录失败: {e}"))?;
    }
    if !runtime_jar.exists() {
        return Err(format!(
            "未找到内置后端包 {jar_name}（请先执行 `mvn package -DskipTests` 后再启动客户端）。"
        ));
    }

    let log_file = std::fs::OpenOptions::new()
        .create(true)
        .append(true)
        .open(logs_dir.join("desktop.log"))
        .map_err(|e| format!("打开日志文件失败: {e}"))?;
    let log_file_err = log_file
        .try_clone()
        .map_err(|e| format!("复制日志句柄失败: {e}"))?;

    Command::new(resolve_java_cmd())
        .arg("-Dfile.encoding=UTF-8")
        .arg(format!(
            "-Dapp.starlight.upload-dir={}",
            upload_dir.to_string_lossy()
        ))
        .arg("-jar")
        .arg(runtime_jar)
        .stdout(Stdio::from(log_file))
        .stderr(Stdio::from(log_file_err))
        .spawn()
        .map_err(|e| format!("启动后端失败: {e}"))
}

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    let app = tauri::Builder::default()
        .plugin(tauri_plugin_opener::init())
        .invoke_handler(tauri::generate_handler![
            get_startup_error,
            is_backend_ready_cmd,
            retry_backend_start,
            open_logs_dir,
            open_runtime_dir
        ])
        .manage(BackendState {
            child: Mutex::new(None),
            startup_error: Mutex::new(None),
        })
        .setup(|app| {
            let state = app.state::<BackendState>();
            if is_backend_ready() {
                if let Ok(mut guard) = state.child.lock() {
                    *guard = None;
                }
                if let Ok(mut err) = state.startup_error.lock() {
                    *err = None;
                }
                return Ok(());
            }

            match start_backend(app.handle()) {
                Ok(child) => {
                    if let Ok(mut guard) = state.child.lock() {
                        *guard = Some(child);
                    }
                    if let Ok(mut err) = state.startup_error.lock() {
                        *err = None;
                    }
                }
                Err(e) => {
                    if let Ok(mut err) = state.startup_error.lock() {
                        *err = Some(format!("{e}（日志：%LOCALAPPDATA%\\FriendHollow\\logs\\desktop.log）"));
                    }
                }
            }
            Ok(())
        })
        .build(tauri::generate_context!())
        .expect("error while building tauri application");

    app.run(|app: &tauri::AppHandle, event| {
        if let tauri::RunEvent::Exit = event {
            let state = app.state::<BackendState>();
            let lock_result = state.child.lock();
            let mut guard = match lock_result {
                Ok(g) => g,
                Err(_) => return,
            };
            if let Some(child) = guard.as_mut() {
                let _ = child.kill();
            }
            *guard = None;
        }
    });
}
