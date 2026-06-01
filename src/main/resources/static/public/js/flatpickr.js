const dateList = document.getElementById("dateList");

// Read existing dates from Backend
const existingDatesElement = document.getElementById("existingDates");
const existingDates = existingDatesElement ? existingDatesElement.value : "";
const parseDates = existingDates ? existingDates.split(",") : "";

// Initialize Flatpickr
const calendar = flatpickr("#availableDates", {
	mode: "multiple",
	inline: true,
	minDate: "today",
	dateFormat: "Y-m-d",
	defaultDate: parseDates,
	
	onChange: function(selectedDates) {
		renderSelectedDates(selectedDates);
	}
});

// Render preloaded dates
renderSelectedDates(calendar.selectedDates);

// Render selected dates
function renderSelectedDates(selectedDates) {
    dateList.innerHTML = "";

    selectedDates.forEach(date => {
        const formatted = calendar.formatDate(date, "Y-m-d");

        const tag = document.createElement("div");
        tag.className = "date-tag";
        tag.innerText = formatted;

        dateList.appendChild(tag);
    });
}

// Form Submit
function submitDates() {

    const selected = calendar.selectedDates.map(date =>
        calendar.formatDate(date, "Y-m-d")
    );
	
	if (selected.length === 0) {
	    alert("Please select at least one available date.");
	    return false;
	}

    document.getElementById("selectedDatesInput").value = selected.join(",");
}