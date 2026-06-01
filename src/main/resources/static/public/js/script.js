const setupModal = () => {
  const modal = document.querySelector("[data-news-modal]");
  if (!modal) return;
  const setOpen = (value) => {
    modal.classList.toggle("is-open", value);
    modal.setAttribute("aria-hidden", String(!value));
    document.body.style.overflow = value ? "hidden" : "";
  };
  document.querySelectorAll("[data-open-news]").forEach((button) => {
    button.addEventListener("click", () => setOpen(true));
  });
  document.querySelectorAll("[data-close-news]").forEach((button) => {
    button.addEventListener("click", () => setOpen(false));
  });
  window.addEventListener("keydown", (event) => {
    if (event.key === "Escape") setOpen(false);
  });
};

const setupChrome = () => {
  const navShell = document.querySelector("[data-nav-shell]");
  const pageTop = document.querySelector("[data-to-top]");
  const mobileMenuButton = document.querySelector("[data-toggle-mobile-menu]");
  const mobileMenuLayer = document.querySelector("[data-mobile-menu-layer]");
  const mobileMenuClosers = document.querySelectorAll("[data-close-mobile-menu]");

  const update = () => {
    navShell?.classList.toggle("navfixed", window.scrollY > 400 && window.innerWidth > 760);
    document.getElementById("pagetop")?.classList.toggle("is-visible", window.scrollY > 300);
    document.body.classList.toggle("show-mobile-cta", window.scrollY > 100);
  };

  const setMobileMenu = (value) => {
    mobileMenuLayer?.classList.toggle("is-open", value);
    mobileMenuLayer?.setAttribute("aria-hidden", String(!value));
    mobileMenuButton?.setAttribute("aria-expanded", String(value));
    document.body.classList.toggle("mobile-menu-open", value);
  };

  mobileMenuButton?.addEventListener("click", () => {
    setMobileMenu(!mobileMenuLayer?.classList.contains("is-open"));
  });
  mobileMenuClosers.forEach((button) => button.addEventListener("click", () => setMobileMenu(false)));
  mobileMenuLayer?.querySelectorAll("a").forEach((link) => {
    link.addEventListener("click", () => setMobileMenu(false));
  });
  window.addEventListener("keydown", (event) => {
    if (event.key === "Escape") setMobileMenu(false);
  });
  pageTop?.addEventListener("click", () => window.scrollTo({ top: 0, behavior: "smooth" }));
  window.addEventListener("scroll", update, { passive: true });
  update();
};

setupModal();
setupChrome();