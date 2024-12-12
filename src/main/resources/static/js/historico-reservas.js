function formatarNome(nomeCompleto) {

    let partes = nomeCompleto.trim().split(/\s+/);
    if (partes.length === 1) {
        return partes[0];
    }

    let primeiroNome = partes[0];
    let ultimoNome = partes[partes.length - 1];
    return `${primeiroNome} ${ultimoNome}`;
}

function atualizarNomeUsuario() {

    let spanNome = document.querySelector('span#nome');
    if (spanNome) {
        let nomeCompleto = spanNome.textContent;
        let nomeFormatado = formatarNome(nomeCompleto);
        spanNome.textContent = nomeFormatado;
    }
}
document.addEventListener('DOMContentLoaded', atualizarNomeUsuario);

