import { Component, OnInit } from '@angular/core';
import {AuthService} from '../servicios/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  user = localStorage.getItem('currentUser');
  constructor(public authService: AuthService) { }

  ngOnInit(): void {
  }

}
