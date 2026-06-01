/*!
 * Start Bootstrap - SB Admin v7.0.7 (https://startbootstrap.com/template/sb-admin)
 * Copyright 2013-2023 Start Bootstrap
 * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
 */
// 
// Scripts
// 

(() => {
  // Helper: run after DOM is ready (or immediately if already ready)
  const onReady = (fn) => {
    if (document.readyState === 'loading') {
      document.addEventListener('DOMContentLoaded', fn, { once: true });
    } else {
      fn();
    }
  };
  


  onReady(() => {
	// =========================
	// 0) Initialize Quill text editor
	// =========================
	const editorElem = document.getElementById('editor');
	if(editorElem) {
		const quill = new Quill(editorElem, {
		  theme: 'snow',
		  modules: {
		      toolbar: [
				[{ 'header': [1, 2, 3, false] }],
		        [{ 'size': ['small', false, 'large', 'huge'] }],
				[{ 'font': [] }],
		        ['bold', 'italic', 'underline', 'strike'],
		        [{ 'color': [] }, { 'background': [] }],
		        [{ 'list': 'ordered' }, { 'list': 'bullet' }],
		        [{ 'align': [] }],
		        ['link'],
		        ['clean']	
		      ]
			},
		  formats: [
			'bold', 'italic', 'underline', 'strike',
		    'color', 'background',
		    'list',
		    'align',
		    'link',	
			'header'
		  ]
		});
		const form = document.getElementById("variantsForm");
		if(form) {
			form.addEventListener('submit',function (){
				document.getElementById('hidden-input').value = quill.root.innerHTML;
			});
		}
	}
    // =========================
    // 1) Toggle the side navigation (SB Admin)
    // =========================
    const sidebarToggle = document.body.querySelector('#sidebarToggle');
    if (sidebarToggle) {
      // Uncomment Below to persist sidebar toggle between refreshes
      // if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
      //   document.body.classList.toggle('sb-sidenav-toggled');
      // }
      sidebarToggle.addEventListener('click', (event) => {
        event.preventDefault();
        document.body.classList.toggle('sb-sidenav-toggled');
        localStorage.setItem(
          'sb|sidebar-toggle',
          document.body.classList.contains('sb-sidenav-toggled')
        );
      });
    }

    // =========================
    // 2) Toggle-input-wrapper (dropdown <-> input)
    // =========================
    document.querySelectorAll('.toggle-input-wrapper').forEach((wrapper) => {
      const dropdown  = wrapper.querySelector('[data-type="dropdown"]');
      const input     = wrapper.querySelector('[data-type="input"]');
      const addBtn    = wrapper.querySelector('[data-type="add"]');
      const removeBtn = wrapper.querySelector('[data-type="remove"]');

      // start state
      if (dropdown) dropdown.disabled = false;
      if (input)    input.disabled = true;

      addBtn?.addEventListener('click', () => {
        dropdown?.classList.add('d-none'); if (dropdown) dropdown.disabled = true;
        addBtn.classList.add('d-none');
        input?.classList.remove('d-none'); if (input) { input.disabled = false; input.focus(); }
        removeBtn?.classList.remove('d-none');
      });

      removeBtn?.addEventListener('click', () => {
        if (input) {
          input.classList.add('d-none'); input.disabled = true; input.value = '';
        }
        removeBtn.classList.add('d-none');
        dropdown?.classList.remove('d-none'); if (dropdown) dropdown.disabled = false;
        addBtn.classList.remove('d-none');
      });
    });

    // =========================
    // 8) Image grid manager (existing/new images, max 10)
    // =========================
    {
      const maxImages    = 10;
      const grid         = document.getElementById('image-grid');
      const addBox       = document.getElementById('add-box');
      const hiddenBucket = document.getElementById('hidden-inputs');
      const toRemove     = document.getElementById('to-remove');
      const inputTpl     = document.getElementById('image-input-template');

      if (grid && addBox && hiddenBucket && toRemove && inputTpl) {
        let existingCount = Number(grid.dataset.existingCount || 0);

        const refresh = () => {
          // reindex ONLY new files
          const newInputs = hiddenBucket.querySelectorAll('input[type="file"]');
          newInputs.forEach((inp, i) => (inp.name = `image[${i}]`));

          const total = existingCount + newInputs.length;
          const atLimit = total >= maxImages;
          addBox.classList.toggle('d-none', atLimit);
          addBox.style.display = atLimit ? 'none' : 'flex';
        };

        const countNew = () =>
          hiddenBucket.querySelectorAll('input[type="file"]').length;

        const addPreviewForNew = (file, input) => {
          const wrap = document.createElement('div');
          wrap.className = 'img-wrapper position-relative';
          Object.assign(wrap.style, {
            width: '120px',
            height: '120px',
            overflow: 'hidden',
            borderRadius: '8px',
            border: '1px solid #ddd'
          });

          const img = document.createElement('img');
          img.src = URL.createObjectURL(file);
          img.alt = file.name;
          Object.assign(img.style, { width: '100%', height: '100%', objectFit: 'cover' });
          img.onload = () => URL.revokeObjectURL(img.src);
          wrap.appendChild(img);

          const btn = document.createElement('button');
          btn.type = 'button';
          btn.textContent = '×';
          btn.className = 'btn btn-sm btn-danger position-absolute top-0 end-0 m-1 p-0';
          Object.assign(btn.style, { width: '24px', height: '24px', borderRadius: '50%', lineHeight: '1' });
          btn.addEventListener('click', () => { wrap.remove(); input.remove(); refresh(); });
          wrap.appendChild(btn);

          grid.insertBefore(wrap, addBox);
        };

        // delete an existing image (already in DB)
        grid.addEventListener('click', (e) => {
          const btn = e.target.closest('.remove-existing');
          if (!btn) return;

          const wrap = btn.closest('.img-wrapper');
          if (!wrap) return;

          const name = wrap.dataset.name; // filename used as identifier
          if (name) {
            const rem = document.createElement('input');
            rem.type  = 'hidden';
            rem.name  = 'removeImageNames'; // controller: List<String> removeImageNames
            rem.value = name;
            toRemove.appendChild(rem);
          }

          wrap.remove();
          existingCount = Math.max(0, existingCount - 1);
          refresh();
        });

        // add a new image
        addBox.addEventListener('click', () => {
          const newCount = countNew();
          if (existingCount + newCount >= maxImages) { refresh(); return; }

          const input = inputTpl.cloneNode(true);
          input.classList.add('d-none');    // keep hidden even if Bootstrap CSS missing
          input.removeAttribute('id');
          input.name = `image[${newCount}]`; // Spring binds to MultipartFile[] image

          input.addEventListener('change', () => {
            if (!input.files || !input.files[0]) { 
              input.remove(); 
              refresh(); 
              return; 
            }
            addPreviewForNew(input.files[0], input);
            refresh();
          });

          hiddenBucket.appendChild(input);
          input.click();
        });

        // initial refresh (replace prior DOMContentLoaded used by this section)
        refresh();
      }
    }
	// =========================
	// 9) Basic image picker + drag & drop previews
	// =========================
	{
	  function initUploader(root) {
	    if (root.dataset.uploaderInit === '1') return; // ✅ guard against double init
	    root.dataset.uploaderInit = '1';

	    const box     = root.querySelector('[data-role="dropzone"]');
	    const input   = root.querySelector('[data-role="input"]');
	    const preview = root.querySelector('[data-role="preview"]');
	    if (!box || !input || !preview) return;

	    const uniqueKey = (f) => `${f.name}::${f.size}::${f.lastModified}`;

	    const renderPreviews = (files) => {
	      preview.innerHTML = '';
	      [...files].forEach((file) => {
	        if (!file?.type?.startsWith('image/')) return;

	        const url  = URL.createObjectURL(file);
	        const wrap = document.createElement('div');
	        wrap.className = 'preview-wrapper position-relative';
	        Object.assign(wrap.style, {
	          width:'240px', height:'240px', border:'1px solid #ddd',
	          borderRadius:'8px', overflow:'hidden'
	        });

	        const img = document.createElement('img');
	        img.className = 'preview-image';
	        Object.assign(img.style, { width:'100%', height:'100%', objectFit:'cover' });
	        img.alt = file.name || 'image';
	        img.src = url;
	        img.addEventListener('load', () => URL.revokeObjectURL(url), { once:true });

	        const btn = document.createElement('button');
	        btn.type = 'button';
	        btn.textContent = '×';
	        btn.className = 'btn btn-sm btn-danger position-absolute top-0 end-0 m-1 p-0';
	        Object.assign(btn.style, { width:'22px', height:'22px', lineHeight:'1', borderRadius:'50%' });
	        btn.addEventListener('click', () => {
	          // rebuild FileList without this file
	          const dt = new DataTransfer();
	          [...input.files].forEach((f) => { if (f !== file) dt.items.add(f); });
	          input.files = dt.files;
	          wrap.remove();
	        });

	        wrap.append(img, btn);
	        preview.appendChild(wrap);
	      });
	    };

	    // Open picker on click ONLY if the dropzone is NOT a LABEL (labels auto-trigger their input)
	    if (box.tagName !== 'LABEL') {
	      box.addEventListener('click', (e) => {
	        // avoid double when clicking directly on the hidden input
	        if (e.target === input) return;
	        input.click();
	      });
	    }

	    // Picker change (single source of truth)
	    input.addEventListener('change', () => {
	      renderPreviews(input.files);
	    });

	    // Drag & drop UX
	    const prevent = (e) => { e.preventDefault(); e.stopPropagation(); };
	    const hilite  = (e) => { prevent(e); box.classList.add('border-info','bg-light'); };
	    const unhi    = (e) => { prevent(e); box.classList.remove('border-info','bg-light'); };

	    ['dragenter','dragover'].forEach((evt) => box.addEventListener(evt, hilite));
	    ['dragleave','drop'].forEach((evt) => box.addEventListener(evt, unhi));

	    box.addEventListener('drop', (e) => {
	      prevent(e);
	      const files = e.dataTransfer?.files;
	      if (!files || !files.length) return;

	      // Merge & dedupe existing + new
	      const dt = new DataTransfer();
	      const seen = new Set();

	      [...input.files, ...files].forEach((f) => {
	        const key = uniqueKey(f);
	        if (!seen.has(key)) { seen.add(key); dt.items.add(f); }
	      });

	      input.files = dt.files;
	      // ❌ no synthetic 'change' here (avoids double processing)
	      renderPreviews(input.files);
	    });
	  }

	  const boot = () => {
	    document.querySelectorAll('[data-component="image-uploader"]').forEach(initUploader);
	  };

	  if (document.readyState === 'loading') {
	    document.addEventListener('DOMContentLoaded', boot, { once: true });
	  } else {
	    boot();
	  }
	}
    // ==== end onReady ====
  });
})();