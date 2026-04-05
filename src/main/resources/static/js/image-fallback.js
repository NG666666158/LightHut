(() => {
    const svg = encodeURIComponent(
        "<svg xmlns='http://www.w3.org/2000/svg' width='1200' height='800' viewBox='0 0 1200 800'>" +
        "<defs><linearGradient id='g' x1='0' y1='0' x2='1' y2='1'>" +
        "<stop offset='0%' stop-color='#fdf3f3'/><stop offset='100%' stop-color='#f6f4ec'/></linearGradient></defs>" +
        "<rect width='1200' height='800' fill='url(#g)'/>" +
        "<text x='50%' y='50%' dominant-baseline='middle' text-anchor='middle' fill='#935252' font-size='40' font-family='sans-serif'>图片加载中</text>" +
        "</svg>"
    );
    const fallbackDataUrl = `data:image/svg+xml;charset=utf-8,${svg}`;

    window.handleImgError = (img) => {
        if (!img || img.dataset.fallbackApplied === "1") {
            return;
        }
        img.dataset.fallbackApplied = "1";
        img.src = img.dataset.fallback || fallbackDataUrl;
    };

    document.addEventListener("error", (event) => {
        if (event.target instanceof HTMLImageElement) {
            window.handleImgError(event.target);
        }
    }, true);
})();
