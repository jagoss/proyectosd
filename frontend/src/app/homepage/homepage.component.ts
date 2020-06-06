import {Component, OnInit} from '@angular/core';
import {AlertService} from '../servicios/alert.service';
import {DispositivoService} from '../servicios/dispositivo.service';
import {AuthService} from '../servicios/auth.service';
import {Router} from '@angular/router';
import {Dispositivo} from "../entities/dispositivo";

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {
  carmesiHexa = '#DC143C';
  verdeHexa = '#3CB371';
  grisHexa;
  bgColorActual = null;
  lightList: Dispositivo[];

  // = [{name: 'lampara 1', encendido: false, bgColor: null}, {name: 'lampara 2', encendido: false, bgColor: null}];

  constructor(private alertService: AlertService,
              private dispositivoService: DispositivoService,
              private router: Router,
              private authService: AuthService) {
    if (!authService.currentUserValue) {
      this.router.navigate(['/login']);
    }
  }

  ngOnInit(): void {
    this.estadoDispositivos();
    // for (const light of this.lightList) {
    //   light.encendido ? light.bgColor = this.verdeHexa : light.bgColor = this.carmesiHexa;
    // }
  }

  cambiarEstado(light) {
    light.encendido = !light.encendido;
    light.encendido ? light.bgColor = this.verdeHexa : light.bgColor = this.carmesiHexa;
  }

  estadoDispositivos() {
    this.dispositivoService.actualizarEstadoDispositivos().subscribe(dispositivos => {
      this.lightList = dispositivos;
      for (let dispositivo of this.lightList) {
        if (dispositivo.conectado) {
          dispositivo.encendido ? dispositivo.bgColor = this.verdeHexa : dispositivo.bgColor = this.carmesiHexa;
        } else {
          dispositivo.bgColor = this.grisHexa;
        }
      }
    });
  }

  actualizarDispositivo(dispSeleccionado: Dispositivo) {
    if (dispSeleccionado.conectado) {

      this.dispositivoService.actualizarDispositivo(dispSeleccionado.id).subscribe(dispositivo => {
        for (let disp of this.lightList) {
          if (dispositivo.id === disp.id) {
            disp = dispositivo;
            if (disp.conectado) {
              disp.encendido ? disp.bgColor = this.verdeHexa : disp.bgColor = this.carmesiHexa;
            } else {
              disp.bgColor = this.grisHexa;
            }
          }
        }
      });

    }
  }
}
