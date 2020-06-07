import {Component, OnInit} from '@angular/core';
import {AlertService} from '../servicios/alert.service';
import {DispositivoService} from '../servicios/dispositivo.service';
import {AuthService} from '../servicios/auth.service';
import {Router} from '@angular/router';
import {Dispositivo} from '../entities/dispositivo';
import {Request} from '../entities/request';

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
  dispList: Dispositivo[];

  // = [{deviceName: 'lampara 1', extension: ["LIGHT", "ON"]}, {name: 'lampara 2', encendido: false, bgColor: null}];

  constructor(private alertService: AlertService,
              private dispositivoService: DispositivoService,
              private router: Router,
              private authService: AuthService) {
    if (!authService.currentUserValue) {
      this.router.navigate(['/login']);
    }
  }

  ngOnInit(): void {
    this.dispositivoService.getCurrentGadgets().subscribe(lista => {
      console.log(lista);
      this.dispList = lista;
    });
    // for (const light of this.lightList) {
    //   light.encendido ? light.bgColor = this.verdeHexa : light.bgColor = this.carmesiHexa;
    // }
  }

  cambiarEstado(light) {
    light.encendido = !light.encendido;
    light.encendido ? light.bgColor = this.verdeHexa : light.bgColor = this.carmesiHexa;
  }

  estadoDispositivos() {
    // this.dispositivoService.actualizarEstadoDispositivos().subscribe(dispositivos => {
    //   this.lightList = dispositivos;
    //   for (let dispositivo of this.lightList) {
    //     if (dispositivo.conectado) {
    //       dispositivo.encendido ? dispositivo.bgColor = this.verdeHexa : dispositivo.bgColor = this.carmesiHexa;
    //     } else {
    //       dispositivo.bgColor = this.grisHexa;
    //     }
    //   }
    // });
  }

  actualizarDispositivo(disp: Dispositivo, order: string) {
    const req: Request = new Request(disp.deviceName, 'LIGHT', order);
    this.dispositivoService.sendOrder(req).subscribe(lista => this.dispList = lista);

    // if (dispSeleccionado.conectado) {
    //
    //   this.dispositivoService.actualizarDispositivo(dispSeleccionado.id).subscribe(dispositivo => {
    //     for (let disp of this.lightList) {
    //       if (dispositivo.id === disp.id) {
    //         disp = dispositivo;
    //         if (disp.conectado) {
    //           disp.encendido ? disp.bgColor = this.verdeHexa : disp.bgColor = this.carmesiHexa;
    //         } else {
    //           disp.bgColor = this.grisHexa;
    //         }
    //       }
    //     }
    //   });
    //
    // }
  }
}
