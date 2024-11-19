document.getElementById('testButton').addEventListener('click', function() {
    const message = document.getElementById('message');
    message.textContent = "¡Has hecho clic en el botón!";
    message.style.color = "green";
});
