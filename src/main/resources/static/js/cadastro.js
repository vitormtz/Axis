// Validação do formulário
document.addEventListener('DOMContentLoaded', function () {
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
});

// Quando o documento estiver pronto (equivalente ao DOMContentLoaded do jQuery)
$(document).ready(function () {
    // Máscara do CPF
    $('#cpf').mask('999.999.999-99');

    // Máscara do telefone
    $('#telefone').mask('(99) 99999-9999');
});