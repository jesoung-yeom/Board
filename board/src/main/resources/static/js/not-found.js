class notFound {

    backButton;

    constructor() {
        this.backButton = document.getElementById("back-button");

        this.backButton.addEventListener("click", this.back);
    }

    back = (event) => {
        event.preventDefault();
        window.location.href = "/home";
    }

}