function initialSetup() {
    otherNamesInput.style.display = "none";
    userNameInput.style.display = "block";
}

function toggleFields() {
    if (searchTypeOtherNames.checked) {
        otherNamesInput.style.display = "block";
        userNameInput.style.display = "none";
    } else {
        otherNamesInput.style.display = "none";
        userNameInput.style.display = "block";
    }
}

var userNameInput = document.getElementById("userNameInput");
var otherNamesInput = document.getElementById("otherNamesInput");
var searchTypeOtherNames = document.getElementById("searchTypeOtherNames");
var searchTypeUserName = document.getElementById("searchTypeUserName");

searchTypeOtherNames.addEventListener("change", toggleFields, false);
searchTypeUserName.addEventListener("change", toggleFields, false);

initialSetup();