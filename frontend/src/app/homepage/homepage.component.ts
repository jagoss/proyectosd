import {Component, OnInit} from '@angular/core';
import {AlertService} from '../servicios/alert.service';
import {LightService} from '../servicios/light.service';
import {AuthService} from '../servicios/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {
  carmesiHexa = '#DC143C';
  verdeHexa = '#3CB371';
  bgColorActual = null;
  lightList = [{name: 'lampara 1', encendido: false, bgColor: null}, {name: 'lampara 2', encendido: false, bgColor: null}];

  constructor(private alertService: AlertService,
              private lightService: LightService,
              private router: Router,
              private authService: AuthService) {
    if (!authService.currentUserValue) {
      this.router.navigate(['/login']);
    }
  }

  ngOnInit(): void {

    for (const light of this.lightList) {
      light.encendido ? light.bgColor = this.verdeHexa : light.bgColor = this.carmesiHexa;
    }
  }

  cambiarEstado(light) {
    light.encendido = !light.encendido;
    light.encendido ? light.bgColor = this.verdeHexa : light.bgColor = this.carmesiHexa;
  }
}
