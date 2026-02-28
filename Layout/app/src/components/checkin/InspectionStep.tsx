import { useState, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Camera, X, Check, AlertCircle } from 'lucide-react';
import { Textarea } from '@/components/ui/textarea';
import { Label } from '@/components/ui/label';
import type { InspectionPhoto, InspectionRegion } from '@/types';

interface InspectionStepProps {
  photos: InspectionPhoto[];
  observations: string;
  onPhotoAdd: (photo: InspectionPhoto) => void;
  onPhotoRemove: (photoId: string) => void;
  onObservationsChange: (value: string) => void;
}

interface RegionConfig {
  id: InspectionRegion;
  label: string;
  x: number;
  y: number;
}

const regions: RegionConfig[] = [
  { id: 'front', label: 'Frente', x: 50, y: 15 },
  { id: 'rear', label: 'Traseira', x: 50, y: 85 },
  { id: 'left_side', label: 'Lateral Esq.', x: 15, y: 50 },
  { id: 'right_side', label: 'Lateral Dir.', x: 85, y: 50 },
  { id: 'wheels', label: 'Rodas', x: 25, y: 75 },
  { id: 'interior', label: 'Interior', x: 50, y: 50 },
];

export function InspectionStep({
  photos,
  observations,
  onPhotoAdd,
  onPhotoRemove,
  onObservationsChange,
}: InspectionStepProps) {
  const [selectedRegion, setSelectedRegion] = useState<InspectionRegion | null>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const getRegionPhotos = (regionId: InspectionRegion) => {
    return photos.filter((p) => p.region === regionId);
  };

  const isRegionComplete = (regionId: InspectionRegion) => {
    return getRegionPhotos(regionId).length > 0;
  };

  const handleRegionClick = (regionId: InspectionRegion) => {
    setSelectedRegion(regionId);
    fileInputRef.current?.click();
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file && selectedRegion) {
      const reader = new FileReader();
      reader.onloadend = () => {
        const newPhoto: InspectionPhoto = {
          id: Date.now().toString(),
          region: selectedRegion,
          url: reader.result as string,
          timestamp: new Date(),
        };
        onPhotoAdd(newPhoto);
        setSelectedRegion(null);
      };
      reader.readAsDataURL(file);
    }
  };

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: { staggerChildren: 0.1 },
    },
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: { opacity: 1, y: 0 },
  };

  return (
    <motion.div
      variants={containerVariants}
      initial="hidden"
      animate="visible"
      className="space-y-6"
    >
      <motion.div variants={itemVariants} className="text-center mb-4">
        <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <Camera className="w-8 h-8 text-blue-600" />
        </div>
        <h2 className="text-2xl font-bold text-gray-900">Inspeção Visual</h2>
        <p className="text-gray-500">
          Clique nas regiões do veículo para adicionar fotos
        </p>
      </motion.div>

      <div className="grid grid-cols-2 gap-8">
        {/* Car Diagram */}
        <motion.div variants={itemVariants} className="relative">
          <div className="relative w-full aspect-[2/3] max-w-sm mx-auto">
            {/* Car Image */}
            <img
              src="/car-diagram.png"
              alt="Diagrama do veículo"
              className="w-full h-full object-contain"
            />

            {/* Hotspots */}
            {regions.map((region, index) => {
              const isComplete = isRegionComplete(region.id);
              const isPending = !isComplete;

              return (
                <motion.button
                  key={region.id}
                  initial={{ scale: 0 }}
                  animate={{ scale: 1 }}
                  transition={{ delay: 0.8 + index * 0.1, type: 'spring', stiffness: 500 }}
                  onClick={() => handleRegionClick(region.id)}
                  className="absolute w-10 h-10 -translate-x-1/2 -translate-y-1/2 group"
                  style={{ left: `${region.x}%`, top: `${region.y}%` }}
                >
                  <motion.div
                    className={`w-full h-full rounded-full flex items-center justify-center shadow-lg transition-all duration-200 ${
                      isComplete
                        ? 'bg-green-500 text-white'
                        : 'bg-red-500 text-white'
                    }`}
                    whileHover={{ scale: 1.3 }}
                    whileTap={{ scale: 0.9 }}
                    animate={
                      isPending
                        ? {
                            boxShadow: [
                              '0 0 0 0 rgba(239, 68, 68, 0.4)',
                              '0 0 0 10px rgba(239, 68, 68, 0)',
                            ],
                            transition: { repeat: Infinity, duration: 1.5 },
                          }
                        : {}
                    }
                  >
                    {isComplete ? (
                      <Check className="w-5 h-5" />
                    ) : (
                      <Camera className="w-5 h-5" />
                    )}
                  </motion.div>

                  {/* Tooltip */}
                  <div className="absolute -bottom-8 left-1/2 -translate-x-1/2 whitespace-nowrap bg-gray-800 text-white text-xs px-2 py-1 rounded opacity-0 group-hover:opacity-100 transition-opacity">
                    {region.label}
                  </div>
                </motion.button>
              );
            })}
          </div>

          {/* Legend */}
          <div className="flex justify-center gap-6 mt-4">
            <div className="flex items-center gap-2">
              <div className="w-4 h-4 rounded-full bg-green-500" />
              <span className="text-sm text-gray-600">Fotografado</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-4 h-4 rounded-full bg-red-500" />
              <span className="text-sm text-gray-600">Pendente</span>
            </div>
          </div>

          <input
            ref={fileInputRef}
            type="file"
            accept="image/*"
            capture="environment"
            onChange={handleFileChange}
            className="hidden"
          />
        </motion.div>

        {/* Photo Grid */}
        <motion.div variants={itemVariants} className="space-y-4">
          <h3 className="font-semibold text-gray-900 flex items-center gap-2">
            <Camera className="w-5 h-5" />
            Fotos Capturadas ({photos.length})
          </h3>

          {photos.length === 0 ? (
            <div className="flex flex-col items-center justify-center h-48 bg-gray-50 rounded-xl border-2 border-dashed border-gray-200">
              <AlertCircle className="w-10 h-10 text-gray-300 mb-2" />
              <p className="text-gray-400 text-sm">Nenhuma foto capturada</p>
              <p className="text-gray-400 text-xs">Clique nas regiões do carro</p>
            </div>
          ) : (
            <div className="grid grid-cols-2 gap-3 max-h-80 overflow-y-auto">
              <AnimatePresence>
                {photos.map((photo, index) => (
                  <motion.div
                    key={photo.id}
                    initial={{ opacity: 0, scale: 0.8, x: 50 }}
                    animate={{ opacity: 1, scale: 1, x: 0 }}
                    exit={{ opacity: 0, scale: 0.8 }}
                    transition={{ delay: index * 0.05 }}
                    className="relative aspect-square rounded-lg overflow-hidden bg-gray-100 group"
                  >
                    <img
                      src={photo.url}
                      alt={`Foto ${photo.region}`}
                      className="w-full h-full object-cover"
                    />
                    <div className="absolute inset-0 bg-black/0 group-hover:bg-black/30 transition-colors" />
                    <div className="absolute bottom-0 left-0 right-0 bg-black/60 text-white text-xs p-1.5">
                      {regions.find((r) => r.id === photo.region)?.label}
                    </div>
                    <button
                      onClick={() => onPhotoRemove(photo.id)}
                      className="absolute top-2 right-2 w-6 h-6 bg-red-500 text-white rounded-full flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity"
                    >
                      <X className="w-4 h-4" />
                    </button>
                  </motion.div>
                ))}
              </AnimatePresence>
            </div>
          )}
        </motion.div>
      </div>

      {/* Observations */}
      <motion.div variants={itemVariants} className="space-y-2">
        <Label htmlFor="observations">Observações da Inspeção</Label>
        <Textarea
          id="observations"
          placeholder="Descreva o estado geral do veículo, danos visíveis, etc."
          value={observations}
          onChange={(e) => onObservationsChange(e.target.value)}
          className="min-h-[100px] resize-none"
        />
      </motion.div>
    </motion.div>
  );
}
