import { motion } from 'framer-motion';
import { Car, Calendar, Gauge } from 'lucide-react';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import type { Vehicle } from '@/types';

interface VehicleStepProps {
  data: Partial<Vehicle>;
  onChange: (data: Partial<Vehicle>) => void;
}

const brands = ['Toyota', 'Honda', 'Volkswagen', 'Ford', 'Chevrolet', 'Fiat', 'Hyundai', 'Renault'];
const colors = ['Preto', 'Branco', 'Prata', 'Cinza', 'Vermelho', 'Azul', 'Verde', 'Amarelo'];

const currentYear = new Date().getFullYear();
const years = Array.from({ length: 30 }, (_, i) => currentYear - i);

export function VehicleStep({ data, onChange }: VehicleStepProps) {
  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: { staggerChildren: 0.08 },
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
      <motion.div variants={itemVariants} className="text-center mb-8">
        <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <Car className="w-8 h-8 text-blue-600" />
        </div>
        <h2 className="text-2xl font-bold text-gray-900">Dados do Veículo</h2>
        <p className="text-gray-500">Informe os dados do veículo para check-in</p>
      </motion.div>

      <div className="grid grid-cols-2 gap-6">
        <motion.div variants={itemVariants} className="space-y-2">
          <Label htmlFor="plate">Placa *</Label>
          <Input
            id="plate"
            placeholder="ABC-1234"
            value={data.plate || ''}
            onChange={(e) => onChange({ ...data, plate: e.target.value.toUpperCase() })}
            className="h-12 text-lg uppercase"
            maxLength={8}
          />
        </motion.div>

        <motion.div variants={itemVariants} className="space-y-2">
          <Label htmlFor="brand">Marca *</Label>
          <Select
            value={data.brand}
            onValueChange={(value) => onChange({ ...data, brand: value })}
          >
            <SelectTrigger className="h-12">
              <SelectValue placeholder="Selecione a marca" />
            </SelectTrigger>
            <SelectContent>
              {brands.map((brand) => (
                <SelectItem key={brand} value={brand}>
                  {brand}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </motion.div>

        <motion.div variants={itemVariants} className="space-y-2">
          <Label htmlFor="model">Modelo *</Label>
          <Input
            id="model"
            placeholder="Ex: Corolla"
            value={data.model || ''}
            onChange={(e) => onChange({ ...data, model: e.target.value })}
            className="h-12"
          />
        </motion.div>

        <motion.div variants={itemVariants} className="space-y-2">
          <Label htmlFor="year">Ano *</Label>
          <Select
            value={data.year?.toString()}
            onValueChange={(value) => onChange({ ...data, year: parseInt(value) })}
          >
            <SelectTrigger className="h-12">
              <Calendar className="w-4 h-4 mr-2 text-gray-400" />
              <SelectValue placeholder="Selecione o ano" />
            </SelectTrigger>
            <SelectContent>
              {years.map((year) => (
                <SelectItem key={year} value={year.toString()}>
                  {year}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </motion.div>

        <motion.div variants={itemVariants} className="space-y-2">
          <Label htmlFor="color">Cor *</Label>
          <Select
            value={data.color}
            onValueChange={(value) => onChange({ ...data, color: value })}
          >
            <SelectTrigger className="h-12">
              <SelectValue placeholder="Selecione a cor" />
            </SelectTrigger>
            <SelectContent>
              {colors.map((color) => (
                <SelectItem key={color} value={color}>
                  {color}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </motion.div>

        <motion.div variants={itemVariants} className="space-y-2">
          <Label htmlFor="mileage">Quilometragem *</Label>
          <div className="relative">
            <Gauge className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
            <Input
              id="mileage"
              type="number"
              placeholder="0"
              value={data.mileage || ''}
              onChange={(e) => onChange({ ...data, mileage: parseInt(e.target.value) || 0 })}
              className="h-12 pl-10"
            />
            <span className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 text-sm">
              km
            </span>
          </div>
        </motion.div>
      </div>
    </motion.div>
  );
}
