const TARGET = "http://127.0.0.1:8080/";
const bootPanel = document.querySelector("#boot-panel");
const appPanel = document.querySelector("#app-panel");
const appFrame = document.querySelector("#app-frame");
const statusEl = document.querySelector("#status");
const retryBtn = document.querySelector("#retry-btn");
const logsBtn = document.querySelector("#open-logs-btn");
const runtimeBtn = document.querySelector("#open-runtime-btn");
const { invoke } = window.__TAURI__.core;

async function showStartupErrorIfAny() {
  try {
    const err = await invoke("get_startup_error");
    if (err) {
      statusEl.textContent = `后端启动失败：${err}`;
      return true;
    }
  } catch (_e) {}
  return false;
}

async function probe() {
  try {
    const ready = await invoke("is_backend_ready_cmd");
    if (ready) {
      statusEl.textContent = "服务已就绪，正在载入主界面...";
      appFrame.src = TARGET;
      bootPanel.classList.add("hidden");
      appPanel.classList.remove("hidden");
      return true;
    }
  } catch (_err) {
    // Invoke transport error (rare), keep waiting loop alive.
  }
  return false;
}

async function loopProbe() {
  if (await showStartupErrorIfAny()) return;
  let tries = 0;
  while (tries < 120) {
    tries += 1;
    if (tries % 5 === 0 && (await showStartupErrorIfAny())) return;
    if (await probe()) return;
    statusEl.textContent = `正在等待服务启动... (${tries}s)`;
    await new Promise((r) => setTimeout(r, 1000));
  }
  statusEl.textContent = "启动超时，请关闭客户端后重试。";
}

retryBtn.addEventListener("click", async () => {
  statusEl.textContent = "正在重新拉起后端服务...";
  try {
    await invoke("retry_backend_start");
  } catch (e) {
    statusEl.textContent = `重试失败：${String(e)}`;
    return;
  }
  void loopProbe();
});

logsBtn.addEventListener("click", async () => {
  try {
    await invoke("open_logs_dir");
  } catch (e) {
    statusEl.textContent = `打开日志目录失败：${String(e)}`;
  }
});

runtimeBtn.addEventListener("click", async () => {
  try {
    await invoke("open_runtime_dir");
  } catch (e) {
    statusEl.textContent = `打开运行目录失败：${String(e)}`;
  }
});

setInterval(() => {
  void showStartupErrorIfAny();
}, 3000);

void loopProbe();

appFrame.addEventListener("error", () => {
  statusEl.textContent = "主界面加载失败，请点击“打开日志目录”查看详细信息。";
  bootPanel.classList.remove("hidden");
  appPanel.classList.add("hidden");
});
