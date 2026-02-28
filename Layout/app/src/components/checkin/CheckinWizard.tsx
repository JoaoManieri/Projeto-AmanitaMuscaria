import { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import { Button } from '@/components/ui/button';
import type { Vehicle, Client, InspectionPhoto, Service } from '@/types';
import { StepIndicator } from './StepIndicator';
import { VehicleStep } from './VehicleStep';
import { ClientStep } from './ClientStep';
import { InspectionStep } from './InspectionStep';
import { SignatureStep } from './SignatureStep';

interface CheckinWizardProps {
  onComplete: (service: Service) => void;
  onCancel: () => void;
}

export function CheckinWizard({ onComplete, onCancel }: CheckinWizardProps) {
  const [currentStep, setCurrentStep] = useState(1);
  const [completedSteps, setCompletedSteps] = useState<number[]>([]);

  // Form data
  const [vehicleData, setVehicleData] = useState<Partial<Vehicle>>({});
  const [clientData, setClientData] = useState<Partial<Client>>({});
  const [inspectionPhotos, setInspectionPhotos] = useState<InspectionPhoto[]>([]);
  const [observations, setObservations] = useState('');
  const [signature, setSignature] = useState('');

  const validateStep = (step: number): boolean => {
    switch (step) {
      case 1:
        return !!(
          vehicleData.plate &&
          vehicleData.brand &&
          vehicleData.model &&
          vehicleData.year &&
          vehicleData.color &&
          vehicleData.mileage
        );
      case 2:
        return !!(clientData.name && clientData.phone);
      case 3:
        return true; // Inspection is optional
      case 4:
        return !!signature;
      default:
        return false;
    }
  };

  const handlePhotoAdd = (photo: InspectionPhoto) => {
    setInspectionPhotos([...inspectionPhotos, photo]);
  };

  const handlePhotoRemove = (photoId: string) => {
    setInspectionPhotos(inspectionPhotos.filter((p) => p.id !== photoId));
  };

  const handleSignature = (sig: string) => {
    setSignature(sig);

    // Create the service object
    const service: Service = {
      id: Date.now().toString(),
      plate: vehicleData.plate!,
      vehicle: vehicleData as Vehicle,
      client: clientData as Client,
      status: 'in_progress',
      entryDate: new Date(),
      observations,
      inspectionPhotos,
      signature: sig,
    };

    onComplete(service);
  };

  const slideVariants = {
    enter: (direction: number) => ({
      x: direction > 0 ? 300 : -300,
      opacity: 0,
    }),
    center: {
      x: 0,
      opacity: 1,
    },
    exit: (direction: number) => ({
      x: direction < 0 ? 300 : -300,
      opacity: 0,
    }),
  };

  const [[page, direction], setPage] = useState([0, 0]);

  const paginate = (newDirection: number) => {
    if (newDirection === 1 && !validateStep(currentStep)) {
      return;
    }

    const newStep = currentStep + newDirection;
    if (newStep >= 1 && newStep <= 4) {
      setPage([page + newDirection, newDirection]);
      setCurrentStep(newStep);

      if (newDirection === 1 && !completedSteps.includes(currentStep)) {
        setCompletedSteps([...completedSteps, currentStep]);
      }
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      className="h-full flex flex-col bg-white"
    >
      {/* Header */}
      <div className="p-6 border-b border-gray-200">
        <div className="flex items-center justify-between mb-4">
          <h1 className="text-2xl font-bold text-gray-900">Novo Check-in</h1>
          <Button variant="ghost" onClick={onCancel}>
            Cancelar
          </Button>
        </div>

        <StepIndicator currentStep={currentStep} completedSteps={completedSteps} />
      </div>

      {/* Content */}
      <div className="flex-1 overflow-auto p-6">
        <div className="max-w-3xl mx-auto">
          <AnimatePresence mode="wait" custom={direction}>
            <motion.div
              key={currentStep}
              custom={direction}
              variants={slideVariants}
              initial="enter"
              animate="center"
              exit="exit"
              transition={{ type: 'spring', stiffness: 300, damping: 30 }}
            >
              {currentStep === 1 && (
                <VehicleStep data={vehicleData} onChange={setVehicleData} />
              )}
              {currentStep === 2 && (
                <ClientStep data={clientData} onChange={setClientData} />
              )}
              {currentStep === 3 && (
                <InspectionStep
                  photos={inspectionPhotos}
                  observations={observations}
                  onPhotoAdd={handlePhotoAdd}
                  onPhotoRemove={handlePhotoRemove}
                  onObservationsChange={setObservations}
                />
              )}
              {currentStep === 4 && <SignatureStep onSign={handleSignature} />}
            </motion.div>
          </AnimatePresence>
        </div>
      </div>

      {/* Footer Navigation */}
      {currentStep < 4 && (
        <div className="p-6 border-t border-gray-200 bg-gray-50">
          <div className="max-w-3xl mx-auto flex justify-between">
            <Button
              variant="outline"
              size="lg"
              onClick={() => paginate(-1)}
              disabled={currentStep === 1}
              className="flex items-center gap-2"
            >
              <ChevronLeft className="w-5 h-5" />
              Voltar
            </Button>

            <Button
              size="lg"
              onClick={() => paginate(1)}
              disabled={!validateStep(currentStep)}
              className="flex items-center gap-2"
            >
              Avançar
              <ChevronRight className="w-5 h-5" />
            </Button>
          </div>
        </div>
      )}
    </motion.div>
  );
}
