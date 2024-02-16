console.log("hello")

const candForm = document.getElementById("candidateForm");
const btn = document.getElementById("candbtn");

btn.onClick = function (){
    if(candForm.style.display !== "none"){
        candForm.style.display = "none"
    }else{
            candForm.style.display = "block"
    }
};
