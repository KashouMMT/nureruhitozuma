const sidePopup = document.getElementById("sidePopup");
const siteHeader = document.getElementById("site-header");

if(sidePopup && 'IntersectionObserver' in window) {
	const show = () => {
		sidePopup.classList.remove("d-none");
	}
	
	const observer = new IntersectionObserver((entries) =>{
		entries.forEach(entry => {
			if (!entry.isIntersecting) {
				show();
			} else {
				sidePopup.classList.add("d-none");
			}
		});
	},{
		threshold: 0.1
	});
	observer.observe(siteHeader);
}