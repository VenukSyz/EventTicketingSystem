import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { ConfigurationFormComponent } from './components/configuration-form/configuration-form.component';
import { Title } from '@angular/platform-browser';
import { filter, map } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ConfigurationFormComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  constructor (private router: Router, private activatedRoute: ActivatedRoute, private titleService: Title ) {}
  
  ngOnInit(): void {
    this.router.events
    .pipe(
      filter((event) => event instanceof NavigationEnd), // Filter NavigationEnd events
      map(() => {
        let route = this.activatedRoute;
        while (route.firstChild) {
          route = route.firstChild; // Traverse to the deepest child route
        }
        return route.snapshot.data['title']; // Get the title from route data
      })
    )
    .subscribe((title: string) => {
      if (title) {
        this.titleService.setTitle(title); // Update the browser tab title
      }
    });
  }

  // title = 'client';
}
