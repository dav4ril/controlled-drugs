function initialSetup() {
    commentsInput.style.display = "none";
    addedInput.style.display = "none";
    batchDatesInput.style.display = "none";
    batchStrInput.style.display = "none";
    newQuantityInput.style.display = "none";
    takeBatchSelectContainer.style.display = "block";
    addBatchSelectContainer.style.display = "none";
    toggleTakeBatchInput();
}

function scrollUp() {
    scroll(0,0);
}

function scrollCreate() {
    scroll(0,530);
}

function scrollEntries() {
    scroll(0,985);
}

function scrollBatches() {
    scroll(0,1595);
}

function toggleFields() {
    if (typeSelector.options[typeSelector.selectedIndex].value == "patient") {
        patientNameInput.style.display = "block";
        batchDatesInput.style.display = "none";
        commentsInput.style.display = "none";
        batchStrInput.style.display = "none";
        addedInput.style.display = "none";
        takenInput.style.display = "block";
        newQuantityInput.style.display = "none";
        takeBatchSelectContainer.style.display = "block";
        addBatchSelectContainer.style.display = "none";
        toggleTakeBatchInput()
    } else if (typeSelector.options[typeSelector.selectedIndex].value == "restock") {
        patientNameInput.style.display = "none";
        commentsInput.style.display = "none";
        batchDatesInput.style.display = "block";
        batchStrInput.style.display = "block";
        addedInput.style.display = "block";
        takenInput.style.display = "none";
        newQuantityInput.style.display = "none";
        takeBatchSelectContainer.style.display = "none";
        addBatchSelectContainer.style.display = "block";
        addBatchSelect.options[addBatchSelect.selectedIndex].defaultSelected = true;
        toggleAddBatchInput()
    } else if (typeSelector.options[typeSelector.selectedIndex].value == "return") {
        patientNameInput.style.display = "none";
        commentsInput.style.display = "block";
        batchDatesInput.style.display = "none";
        batchStrInput.style.display = "none";
        addedInput.style.display = "none";
        takenInput.style.display = "block";
        newQuantityInput.style.display = "none";
        takeBatchSelectContainer.style.display = "block";
        takeBatchSelect.options[takeBatchSelect.selectedIndex].defaultSelected = true;
        addBatchSelectContainer.style.display = "none";
        toggleTakeBatchInput()
    } else if (typeSelector.options[typeSelector.selectedIndex].value == "error") {
        patientNameInput.style.display = "none";
        commentsInput.style.display = "block";
        addedInput.style.display = "none";
        batchDatesInput.style.display = "none";
        batchStrInput.style.display = "none";
        takenInput.style.display = "none";
        newQuantityInput.style.display = "block";
        takeBatchSelectContainer.style.display = "none";
        addBatchSelectContainer.style.display = "none";
    }
}

function toggleAddBatchInput() {
    if (addBatchSelect.options[addBatchSelect.selectedIndex].value == "newbatch") {
        batchStrInput.style.display = "block";
        batchDatesInput.style.display = "block";
    } else {
        batchStrInput.style.display = "none";
        batchDatesInput.style.display = "none";
    }
}

function toggleTakeBatchInput() {
    if (typeSelector.options[typeSelector.selectedIndex].value == "return") {
        batchStrInput.style.display = "none";
        batchDatesInput.style.display = "none";
    } else if (takeBatchSelect.options[takeBatchSelect.selectedIndex].value == "notlisted") {
        batchStrInput.style.display = "block";
        batchDatesInput.style.display = "block";
    } else {
        batchStrInput.style.display = "none";
        batchDatesInput.style.display = "none";
    }
}

var patientNameInput = document.getElementById('patientNameInput');
var typeSelector = document.getElementById('typeSelector');
var commentsInput = document.getElementById('commentsInput');
var batchDatesInput = document.getElementById('batchDatesInput');
var batchStrInput = document.getElementById('batchStrInput');
var takenInput = document.getElementById('takenInput');
var addedInput = document.getElementById('addedInput');
var newQuantityInput = document.getElementById('newQuantityInput');
var addBatchSelectContainer = document.getElementById('addBatchSelectContainer');
var takeBatchSelectContainer = document.getElementById('takeBatchSelectContainer');
var addBatchSelect = document.getElementById('addBatchSelect');
var takeBatchSelect = document.getElementById('takeBatchSelect');
var scrollToTop = document.getElementById('scrollToTop');
var scrollToEntries = document.getElementById('scrollToEntries');
var scrollToCreate = document.getElementById('scrollToCreate');
var scrollToBatches = document.getElementById('scrollToBatches');
var pointer = document.getElementById("myP")

typeSelector.addEventListener('change', toggleFields, false);
scrollToTop.addEventListener('click', scrollUp, false);
scrollToCreate.addEventListener('click', scrollCreate, false);
scrollToEntries.addEventListener('click', scrollEntries, false);
scrollToBatches.addEventListener('click', scrollBatches, false);
addBatchSelect.addEventListener('change', toggleAddBatchInput, false);
takeBatchSelect.addEventListener('change', toggleTakeBatchInput, false);
scrollToCreate.style.cursor = "pointer";
scrollToTop.style.cursor = "pointer";
scrollToEntries.style.cursor = "pointer";
scrollToBatches.style.cursor = "pointer";

initialSetup();