import { Routes } from '@angular/router';
import { ConfigurationFormComponent } from './components/configuration-form/configuration-form.component';
import { ControlPanelComponent } from './components/control-panel/control-panel.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { PastEventsComponent } from './components/past-events/past-events.component';

export const routes: Routes = [

    { 
        path: '',
        redirectTo: 'configuration-form',
        pathMatch:'full'
    },
    {
        path: '',
        component: NavbarComponent,
        children: [
            {
                path: 'configuration-form',
                component: ConfigurationFormComponent,
                data: { title: 'Event Ticketing System | Configuration Form'}
            },
            {
                path: 'control-panel',
                component: ControlPanelComponent,
                data: { title: 'Event Ticketing System | Control Panel'}
            },
            {
                path: 'past-events',
                component: PastEventsComponent,
                data: { title: 'Event Ticketing System | Past Events'}
            }
        ]
    }
    
];
