import { Routes } from '@angular/router';
import { ConfigurationFormComponent } from './components/configuration-form/configuration-form.component';
import { ControlPanelComponent } from './components/control-panel/control-panel.component';
import { TicketStatusComponent } from './components/ticket-status/ticket-status.component';
import { NavbarComponent } from './components/navbar/navbar.component';

export const routes: Routes = [

    {
        path: '',
        component: NavbarComponent,
        children: [
            {
                path: 'configuration-form',
                component: ConfigurationFormComponent
            },
            {
                path: 'control-panel',
                component: ControlPanelComponent
            },
            {
                path: 'ticket-status',
                component: TicketStatusComponent
            }
        ]
    }
    
];