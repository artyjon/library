document.addEventListener('DOMContentLoaded', function() {
    const errorMessageElement = document.getElementById('errorMessage');
    if (errorMessageElement) {
        const errorMessage = errorMessageElement.textContent.trim();
        if (errorMessage) {
            showCustomAlert(errorMessage);
        }
    }
});


function showCustomAlert(message) {
    const alertBox = document.createElement('div');
    alertBox.textContent = message;

    // Основные стили
    alertBox.style.position = 'fixed';
    alertBox.style.top = '-100px'; // Начальная позиция за пределами экрана
    alertBox.style.left = '50%';
    alertBox.style.transform = 'translateX(-50%)';
    alertBox.style.padding = '20px 30px';
    alertBox.style.backgroundColor = '#f44336';
    alertBox.style.color = 'white';
    alertBox.style.fontSize = '16px';
    alertBox.style.fontWeight = 'bold';
    alertBox.style.borderRadius = '10px';
    alertBox.style.boxShadow = '0 10px 20px rgba(0, 0, 0, 0.3)';
    alertBox.style.zIndex = '1000';
    alertBox.style.opacity = '0';
    alertBox.style.cursor = 'pointer';
    alertBox.style.transition = 'top 0.5s ease, opacity 0.5s ease';

    document.body.appendChild(alertBox);

    setTimeout(() => {
        alertBox.style.top = '20px'; // Выезжает сверху
        alertBox.style.opacity = '1';
    }, 100);

    const timeoutId = setTimeout(() => {
        hideAlert(alertBox);
    }, 3000);

    alertBox.addEventListener('click', () => {
        clearTimeout(timeoutId);
        hideAlert(alertBox);
    });
}

function hideAlert(alertBox) {
    alertBox.style.top = '-100px';
    alertBox.style.opacity = '0';
    setTimeout(() => {
        alertBox.remove();
    }, 500); // Ждём завершения анимации
}