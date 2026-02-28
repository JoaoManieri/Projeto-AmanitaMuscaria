import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import type { Service, ServiceStatus, WorkshopSettings, InspectionPhoto } from '@/types';

interface ServiceStore {
  services: Service[];
  selectedServiceId: string | null;
  addService: (service: Service) => void;
  updateService: (id: string, updates: Partial<Service>) => void;
  deleteService: (id: string) => void;
  selectService: (id: string | null) => void;
  completeService: (id: string, signature: string) => void;
  addInspectionPhoto: (serviceId: string, photo: InspectionPhoto) => void;
  removeInspectionPhoto: (serviceId: string, photoId: string) => void;
  getSelectedService: () => Service | null;
  getActiveServices: () => Service[];
  getCompletedServices: () => Service[];
}

const sampleServices: Service[] = [
  {
    id: '1',
    plate: 'ABC-1234',
    vehicle: {
      plate: 'ABC-1234',
      brand: 'Toyota',
      model: 'Corolla',
      year: 2020,
      color: 'Prata',
      mileage: 45000,
    },
    client: {
      name: 'João Silva',
      phone: '(11) 98765-4321',
      email: 'joao.silva@email.com',
    },
    status: 'in_progress',
    entryDate: new Date('2024-01-15T09:00:00'),
    observations: 'Troca de óleo e revisão dos 40.000km. Verificar freios dianteiros.',
    inspectionPhotos: [],
  },
  {
    id: '2',
    plate: 'DEF-5678',
    vehicle: {
      plate: 'DEF-5678',
      brand: 'Honda',
      model: 'Civic',
      year: 2022,
      color: 'Preto',
      mileage: 15000,
    },
    client: {
      name: 'Maria Santos',
      phone: '(11) 91234-5678',
      email: 'maria.santos@email.com',
    },
    status: 'waiting_pickup',
    entryDate: new Date('2024-01-14T14:30:00'),
    observations: 'Alinhamento e balanceamento. Verificar suspensão.',
    inspectionPhotos: [],
  },
  {
    id: '3',
    plate: 'GHI-9012',
    vehicle: {
      plate: 'GHI-9012',
      brand: 'Volkswagen',
      model: 'Golf',
      year: 2019,
      color: 'Branco',
      mileage: 62000,
    },
    client: {
      name: 'Pedro Oliveira',
      phone: '(11) 94567-8901',
    },
    status: 'completed',
    entryDate: new Date('2024-01-10T10:00:00'),
    exitDate: new Date('2024-01-12T16:00:00'),
    observations: 'Reparo no sistema de ar condicionado.',
    inspectionPhotos: [],
  },
];

export const useServiceStore = create<ServiceStore>()(
  persist(
    (set, get) => ({
      services: sampleServices,
      selectedServiceId: null,

      addService: (service) => {
        set((state) => ({
          services: [service, ...state.services],
        }));
      },

      updateService: (id, updates) => {
        set((state) => ({
          services: state.services.map((s) =>
            s.id === id ? { ...s, ...updates } : s
          ),
        }));
      },

      deleteService: (id) => {
        set((state) => ({
          services: state.services.filter((s) => s.id !== id),
          selectedServiceId: state.selectedServiceId === id ? null : state.selectedServiceId,
        }));
      },

      selectService: (id) => {
        set({ selectedServiceId: id });
      },

      completeService: (id, signature) => {
        set((state) => ({
          services: state.services.map((s) =>
            s.id === id
              ? { ...s, status: 'completed' as ServiceStatus, exitDate: new Date(), signature }
              : s
          ),
        }));
      },

      addInspectionPhoto: (serviceId, photo) => {
        set((state) => ({
          services: state.services.map((s) =>
            s.id === serviceId
              ? { ...s, inspectionPhotos: [...s.inspectionPhotos, photo] }
              : s
          ),
        }));
      },

      removeInspectionPhoto: (serviceId, photoId) => {
        set((state) => ({
          services: state.services.map((s) =>
            s.id === serviceId
              ? { ...s, inspectionPhotos: s.inspectionPhotos.filter((p) => p.id !== photoId) }
              : s
          ),
        }));
      },

      getSelectedService: () => {
        const { services, selectedServiceId } = get();
        return services.find((s) => s.id === selectedServiceId) || null;
      },

      getActiveServices: () => {
        return get().services.filter(
          (s) => s.status === 'in_progress' || s.status === 'waiting_pickup'
        );
      },

      getCompletedServices: () => {
        return get().services.filter((s) => s.status === 'completed');
      },
    }),
    {
      name: 'autocheck-services',
    }
  )
);

interface SettingsStore {
  settings: WorkshopSettings;
  updateSettings: (settings: Partial<WorkshopSettings>) => void;
}

const defaultSettings: WorkshopSettings = {
  workshopName: 'Oficina AutoCheck Pro',
  address: 'Rua Exemplo, 123 - São Paulo/SP',
  phone: '(11) 3333-4444',
  cnpj: '12.345.678/0001-90',
  reportHeader: 'Relatório de Serviço',
  showLogoInReport: true,
  requireSignature: true,
};

export const useSettingsStore = create<SettingsStore>()(
  persist(
    (set) => ({
      settings: defaultSettings,
      updateSettings: (newSettings) => {
        set((state) => ({
          settings: { ...state.settings, ...newSettings },
        }));
      },
    }),
    {
      name: 'autocheck-settings',
    }
  )
);
