export interface Vehicle {
  plate: string;
  brand: string;
  model: string;
  year: number;
  color: string;
  mileage: number;
}

export interface Client {
  name: string;
  phone: string;
  email?: string;
  document?: string;
}

export interface InspectionPhoto {
  id: string;
  region: string;
  url: string;
  timestamp: Date;
}

export type ServiceStatus = 'in_progress' | 'waiting_pickup' | 'completed';

export interface Service {
  id: string;
  plate: string;
  vehicle: Vehicle;
  client: Client;
  status: ServiceStatus;
  entryDate: Date;
  exitDate?: Date;
  observations: string;
  inspectionPhotos: InspectionPhoto[];
  signature?: string;
}

export interface WorkshopSettings {
  workshopName: string;
  workshopLogo?: string;
  address: string;
  phone: string;
  cnpj: string;
  reportHeader: string;
  showLogoInReport: boolean;
  requireSignature: boolean;
}

export type InspectionRegion = 
  | 'front' 
  | 'rear' 
  | 'left_side' 
  | 'right_side' 
  | 'wheels' 
  | 'interior';

export interface InspectionRegionConfig {
  id: InspectionRegion;
  label: string;
  x: number;
  y: number;
  completed: boolean;
}
