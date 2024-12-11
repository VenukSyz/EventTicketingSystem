import { CommonModule } from '@angular/common';
import { Component, HostListener } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterOutlet, RouterLinkActive, CommonModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  /** Tracks whether the navbar should appear scrolled (e.g., apply a shadow or background change). */
  isScrolled: boolean = false;

  /**
   * Listens to the window scroll event and updates `isScrolled` based on the scroll position.
   * Sets `isScrolled` to true if the vertical scroll exceeds 50 pixels.
   */
  @HostListener('window:scroll', [])
  onWindowScroll() {
    const scrollY = window.scrollY || document.documentElement.scrollTop || document.body.scrollTop;
    this.isScrolled = scrollY > 50; // Set a threshold (50px in this case)
  }
  
}
