document.addEventListener('DOMContentLoaded', function () {
    // Validação do formulário
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });

    // Toggle de visibilidade da senha
    const togglePassword = document.querySelector('#togglePassword');
    const senhaInput = document.querySelector('#senha');

    if (togglePassword && senhaInput) {
        togglePassword.addEventListener('click', function () {
            // Alterna entre password e text
            const type = senhaInput.getAttribute('type') === 'password' ? 'text' : 'password';
            senhaInput.setAttribute('type', type);

            // Alterna o ícone
            const icon = this.querySelector('i');
            icon.classList.toggle('bi-eye');
            icon.classList.toggle('bi-eye-slash');
        });
    }
});