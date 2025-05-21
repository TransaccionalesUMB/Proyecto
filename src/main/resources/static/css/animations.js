/**
 * StockMaster - Archivo de animaciones JavaScript
 * Este archivo contiene funciones para mejorar la experiencia de usuario
 * con animaciones y efectos visuales.
 */

document.addEventListener('DOMContentLoaded', function() {
    // Inicializar efectos de animación para elementos con la clase animate-fade-in-up
    const animatedElements = document.querySelectorAll('.animate-fade-in-up');
    
    // Configurar el observador para detectar cuando los elementos entran en el viewport
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('animated');
                observer.unobserve(entry.target);
            }
        });
    }, {
        threshold: 0.1
    });
    
    // Observar todos los elementos animados
    animatedElements.forEach(element => {
        observer.observe(element);
    });
    
    // Animar iconos
    const animatedIcons = document.querySelectorAll('.icon-animated');
    animatedIcons.forEach(icon => {
        icon.addEventListener('mouseenter', function() {
            this.classList.add('icon-pulse');
        });
        
        icon.addEventListener('mouseleave', function() {
            this.classList.remove('icon-pulse');
        });
    });
    
    // Efecto de hover para botones con animación
    const animatedButtons = document.querySelectorAll('.btn-animate');
    animatedButtons.forEach(button => {
        button.addEventListener('mouseenter', function() {
            this.classList.add('btn-pulse');
        });
        
        button.addEventListener('mouseleave', function() {
            this.classList.remove('btn-pulse');
        });
    });
    
    // Efecto para tarjetas corporativas
    const corporateCards = document.querySelectorAll('.corporate-card');
    corporateCards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.classList.add('card-hover');
        });
        
        card.addEventListener('mouseleave', function() {
            this.classList.remove('card-hover');
        });
    });
    
    // Inicializar tooltips de Bootstrap si existen
    if (typeof bootstrap !== 'undefined' && bootstrap.Tooltip) {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
    }
});

// Función para animar contadores (alternativa a jQuery)
function animateCounters() {
    const counters = document.querySelectorAll('.counter');
    
    counters.forEach(counter => {
        const target = parseInt(counter.innerText);
        let count = 0;
        const duration = 1500; // duración en milisegundos
        const increment = target / (duration / 16); // 60fps aproximadamente
        
        const updateCount = () => {
            count += increment;
            if (count < target) {
                counter.innerText = Math.floor(count);
                requestAnimationFrame(updateCount);
            } else {
                counter.innerText = target;
            }
        };
        
        updateCount();
    });
}

// Función para manejar efectos de tarjetas sin jQuery
function initializeCardEffects() {
    const cards = document.querySelectorAll('.card-hover-effect');
    
    cards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.classList.add('card-hover-active');
        });
        
        card.addEventListener('mouseleave', function() {
            this.classList.remove('card-hover-active');
        });
    });
}

// Ejecutar animaciones si no se está usando jQuery
if (typeof jQuery === 'undefined') {
    document.addEventListener('DOMContentLoaded', function() {
        animateCounters();
        initializeCardEffects();
    });
}
