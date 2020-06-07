export class Request {

  deviceName: string;
  extension: string;
  value: string;

  constructor(deviceName: string, ext: string, val: string) {
    this.deviceName = deviceName;
    this.extension = ext;
    this.value = val;
  }

}
