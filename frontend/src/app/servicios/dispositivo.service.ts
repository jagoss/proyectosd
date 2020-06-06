import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Dispositivo} from "../entities/dispositivo";

@Injectable({
  providedIn: 'root'
})
export class DispositivoService {
  private getDispositivosUrl = 'http://localhost:8081/dispositivos';
  private getDispositivoUrl = 'http://localhost:8081/dispositivos/';

  constructor(private httpClient: HttpClient) { }

  actualizarEstadoDispositivos() {
    return this.httpClient.get<Dispositivo[]>(this.getDispositivosUrl);
  }

  actualizarDispositivo(id: number) {
    return this.httpClient.patch<Dispositivo>(this.getDispositivoUrl + id, null)
  }
}
