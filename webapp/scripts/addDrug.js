function eraseValue() {
    if (!changed) {
        yearInput.value = "";
        changed = true;
    }
}

var yearInput = document.getElementById('yeartext');

yearInput.addEventListener('click', eraseValue, false);
var changed = false;