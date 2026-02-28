import { motion } from 'framer-motion';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';
import {
  Car,
  User,
  Phone,
  Mail,
  Calendar,
  Gauge,
  Palette,
  FileText,
  CheckCircle,
  FileOutput,
  Edit3,
  Camera,
} from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Separator } from '@/components/ui/separator';
import type { Service } from '@/types';
import { StatusBadge } from '@/components/shared/StatusBadge';
import { EmptyState } from './EmptyState';

interface ServiceDetailsProps {
  service: Service | null;
  onCheckout: () => void;
  onGenerateReport: () => void;
  onEdit: () => void;
}

export function ServiceDetails({
  service,
  onCheckout,
  onGenerateReport,
  onEdit,
}: ServiceDetailsProps) {
  if (!service) {
    return <EmptyState />;
  }

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.05,
        delayChildren: 0.1,
      },
    },
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 10 },
    visible: { opacity: 1, y: 0 },
  };

  return (
    <motion.div
      initial={{ opacity: 0, x: 50 }}
      animate={{ opacity: 1, x: 0 }}
      transition={{ duration: 0.4, delay: 0.15, ease: [0.16, 1, 0.3, 1] }}
      className="h-full overflow-auto p-6"
    >
      <motion.div
        variants={containerVariants}
        initial="hidden"
        animate="visible"
        className="max-w-4xl mx-auto space-y-6"
      >
        {/* Header */}
        <motion.div
          variants={itemVariants}
          className="flex items-start justify-between"
        >
          <div>
            <h1 className="text-4xl font-bold text-gray-900 mb-2">
              {service.plate}
            </h1>
            <StatusBadge status={service.status} size="lg" />
          </div>

          <div className="flex gap-3">
            {service.status !== 'completed' && (
              <motion.div whileHover={{ scale: 1.05 }} whileTap={{ scale: 0.95 }}>
                <Button
                  onClick={onCheckout}
                  size="lg"
                  className="bg-green-600 hover:bg-green-700"
                >
                  <CheckCircle className="w-5 h-5 mr-2" />
                  Fazer Check-out
                </Button>
              </motion.div>
            )}
            <motion.div whileHover={{ scale: 1.05 }} whileTap={{ scale: 0.95 }}>
              <Button onClick={onGenerateReport} size="lg" variant="outline">
                <FileOutput className="w-5 h-5 mr-2" />
                Gerar Relatório
              </Button>
            </motion.div>
            <motion.div whileHover={{ scale: 1.05 }} whileTap={{ scale: 0.95 }}>
              <Button onClick={onEdit} size="lg" variant="ghost">
                <Edit3 className="w-5 h-5" />
              </Button>
            </motion.div>
          </div>
        </motion.div>

        {/* Vehicle Info Card */}
        <motion.div variants={itemVariants}>
          <Card>
            <CardHeader className="pb-3">
              <CardTitle className="flex items-center gap-2 text-lg">
                <Car className="w-5 h-5 text-blue-600" />
                Dados do Veículo
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-3 gap-6">
                <div>
                  <p className="text-sm text-gray-500 mb-1">Marca</p>
                  <p className="font-semibold text-gray-900">{service.vehicle.brand}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-500 mb-1">Modelo</p>
                  <p className="font-semibold text-gray-900">{service.vehicle.model}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-500 mb-1">Ano</p>
                  <p className="font-semibold text-gray-900">{service.vehicle.year}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-500 mb-1">Cor</p>
                  <p className="font-semibold text-gray-900 flex items-center gap-2">
                    <Palette className="w-4 h-4" />
                    {service.vehicle.color}
                  </p>
                </div>
                <div>
                  <p className="text-sm text-gray-500 mb-1">Quilometragem</p>
                  <p className="font-semibold text-gray-900 flex items-center gap-2">
                    <Gauge className="w-4 h-4" />
                    {service.vehicle.mileage.toLocaleString()} km
                  </p>
                </div>
                <div>
                  <p className="text-sm text-gray-500 mb-1">Placa</p>
                  <p className="font-semibold text-gray-900">{service.vehicle.plate}</p>
                </div>
              </div>
            </CardContent>
          </Card>
        </motion.div>

        {/* Client Info Card */}
        <motion.div variants={itemVariants}>
          <Card>
            <CardHeader className="pb-3">
              <CardTitle className="flex items-center gap-2 text-lg">
                <User className="w-5 h-5 text-blue-600" />
                Dados do Cliente
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-2 gap-6">
                <div>
                  <p className="text-sm text-gray-500 mb-1">Nome</p>
                  <p className="font-semibold text-gray-900">{service.client.name}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-500 mb-1">Telefone</p>
                  <p className="font-semibold text-gray-900 flex items-center gap-2">
                    <Phone className="w-4 h-4" />
                    {service.client.phone}
                  </p>
                </div>
                {service.client.email && (
                  <div className="col-span-2">
                    <p className="text-sm text-gray-500 mb-1">Email</p>
                    <p className="font-semibold text-gray-900 flex items-center gap-2">
                      <Mail className="w-4 h-4" />
                      {service.client.email}
                    </p>
                  </div>
                )}
              </div>
            </CardContent>
          </Card>
        </motion.div>

        {/* Service Info */}
        <motion.div variants={itemVariants}>
          <Card>
            <CardHeader className="pb-3">
              <CardTitle className="flex items-center gap-2 text-lg">
                <FileText className="w-5 h-5 text-blue-600" />
                Informações do Atendimento
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex items-center gap-4">
                <div className="flex items-center gap-2 text-gray-600">
                  <Calendar className="w-4 h-4" />
                  <span className="text-sm">
                    Entrada:{' '}
                    {format(new Date(service.entryDate), "dd/MM/yyyy 'às' HH:mm", {
                      locale: ptBR,
                    })}
                  </span>
                </div>
                {service.exitDate && (
                  <div className="flex items-center gap-2 text-gray-600">
                    <CheckCircle className="w-4 h-4 text-green-500" />
                    <span className="text-sm">
                      Saída:{' '}
                      {format(new Date(service.exitDate), "dd/MM/yyyy 'às' HH:mm", {
                        locale: ptBR,
                      })}
                    </span>
                  </div>
                )}
              </div>

              <Separator />

              <div>
                <p className="text-sm text-gray-500 mb-2">Observações</p>
                <p className="text-gray-700 bg-gray-50 p-4 rounded-lg">
                  {service.observations || 'Nenhuma observação registrada.'}
                </p>
              </div>
            </CardContent>
          </Card>
        </motion.div>

        {/* Inspection Photos */}
        {service.inspectionPhotos.length > 0 && (
          <motion.div variants={itemVariants}>
            <Card>
              <CardHeader className="pb-3">
                <CardTitle className="flex items-center gap-2 text-lg">
                  <Camera className="w-5 h-5 text-blue-600" />
                  Fotos da Inspeção
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="grid grid-cols-4 gap-4">
                  {service.inspectionPhotos.map((photo, index) => (
                    <motion.div
                      key={photo.id}
                      initial={{ opacity: 0, scale: 0.8 }}
                      animate={{ opacity: 1, scale: 1 }}
                      transition={{ delay: index * 0.1 }}
                      className="relative aspect-square rounded-lg overflow-hidden bg-gray-100"
                    >
                      <img
                        src={photo.url}
                        alt={`Foto ${photo.region}`}
                        className="w-full h-full object-cover"
                      />
                      <div className="absolute bottom-0 left-0 right-0 bg-black/50 text-white text-xs p-1 text-center">
                        {photo.region}
                      </div>
                    </motion.div>
                  ))}
                </div>
              </CardContent>
            </Card>
          </motion.div>
        )}
      </motion.div>
    </motion.div>
  );
}
