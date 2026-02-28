import { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import type { Service } from '@/types';
import { useServiceStore } from '@/store';
import { ServiceList } from '@/components/dashboard/ServiceList';
import { ServiceDetails } from '@/components/dashboard/ServiceDetails';
import { CheckinWizard } from '@/components/checkin/CheckinWizard';
import { toast } from 'sonner';

export function Dashboard() {
  const [showWizard, setShowWizard] = useState(false);
  const { services, selectedServiceId, addService, selectService, completeService } =
    useServiceStore();

  const activeServices = services.filter(
    (s) => s.status === 'in_progress' || s.status === 'waiting_pickup'
  );

  const selectedService = services.find((s) => s.id === selectedServiceId) || null;

  const handleNewCheckin = () => {
    setShowWizard(true);
  };

  const handleWizardComplete = (service: Service) => {
    addService(service);
    setShowWizard(false);
    toast.success('Check-in realizado com sucesso!', {
      description: `Veículo ${service.plate} registrado.`,
    });
  };

  const handleWizardCancel = () => {
    setShowWizard(false);
  };

  const handleCheckout = () => {
    if (selectedService) {
      // In a real app, this would open a signature modal
      // For now, we'll just mark it as completed
      completeService(selectedService.id, '');
      toast.success('Check-out realizado com sucesso!', {
        description: `Veículo ${selectedService.plate} finalizado.`,
      });
    }
  };

  const handleGenerateReport = () => {
    toast.info('Gerando relatório...', {
      description: 'Funcionalidade em desenvolvimento.',
    });
  };

  const handleEdit = () => {
    toast.info('Editando atendimento...', {
      description: 'Funcionalidade em desenvolvimento.',
    });
  };

  return (
    <div className="h-full">
      <AnimatePresence mode="wait">
        {showWizard ? (
          <motion.div
            key="wizard"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className="h-full"
          >
            <CheckinWizard
              onComplete={handleWizardComplete}
              onCancel={handleWizardCancel}
            />
          </motion.div>
        ) : (
          <motion.div
            key="dashboard"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className="h-full flex"
          >
            {/* Left Panel - Service List */}
            <div className="w-[35%] h-full border-r border-gray-200">
              <ServiceList
                services={activeServices}
                selectedId={selectedServiceId}
                onSelect={selectService}
                onNewCheckin={handleNewCheckin}
              />
            </div>

            {/* Right Panel - Service Details */}
            <div className="flex-1 h-full bg-gray-50/50">
              <ServiceDetails
                service={selectedService}
                onCheckout={handleCheckout}
                onGenerateReport={handleGenerateReport}
                onEdit={handleEdit}
              />
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
}
