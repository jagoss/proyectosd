import {Component, OnInit} from '@angular/core';
import {AuthService} from '../servicios/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  user: string;

  constructor(private authService: AuthService,
              private router: Router) {
    this.user = localStorage.getItem('currentUser');
  }

  ngOnInit(): void {
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

}
