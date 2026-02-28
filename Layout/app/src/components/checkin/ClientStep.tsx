import { motion } from 'framer-motion';
import { User, Phone, Mail, FileText } from 'lucide-react';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import type { Client } from '@/types';

interface ClientStepProps {
  data: Partial<Client>;
  onChange: (data: Partial<Client>) => void;
}

export function ClientStep({ data, onChange }: ClientStepProps) {
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

  const formatPhone = (value: string) => {
    const numbers = value.replace(/\D/g, '');
    if (numbers.length <= 10) {
      return numbers.replace(/(\d{2})(\d{4})(\d{4})/, '($1) $2-$3');
    }
    return numbers.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
  };

  const formatDocument = (value: string) => {
    const numbers = value.replace(/\D/g, '');
    if (numbers.length <= 11) {
      return numbers.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
    }
    return numbers.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, '$1.$2.$3/$4-$5');
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
          <User className="w-8 h-8 text-blue-600" />
        </div>
        <h2 className="text-2xl font-bold text-gray-900">Dados do Cliente</h2>
        <p className="text-gray-500">Informe os dados do proprietário do veículo</p>
      </motion.div>

      <div className="grid grid-cols-2 gap-6">
        <motion.div variants={itemVariants} className="space-y-2 col-span-2">
          <Label htmlFor="name">Nome completo *</Label>
          <div className="relative">
            <User className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
            <Input
              id="name"
              placeholder="Ex: João da Silva"
              value={data.name || ''}
              onChange={(e) => onChange({ ...data, name: e.target.value })}
              className="h-12 pl-10"
            />
          </div>
        </motion.div>

        <motion.div variants={itemVariants} className="space-y-2">
          <Label htmlFor="phone">Telefone *</Label>
          <div className="relative">
            <Phone className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
            <Input
              id="phone"
              placeholder="(11) 98765-4321"
              value={data.phone || ''}
              onChange={(e) => onChange({ ...data, phone: formatPhone(e.target.value) })}
              className="h-12 pl-10"
              maxLength={15}
            />
          </div>
        </motion.div>

        <motion.div variants={itemVariants} className="space-y-2">
          <Label htmlFor="document">CPF/CNPJ</Label>
          <div className="relative">
            <FileText className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
            <Input
              id="document"
              placeholder="000.000.000-00"
              value={data.document || ''}
              onChange={(e) => onChange({ ...data, document: formatDocument(e.target.value) })}
              className="h-12 pl-10"
              maxLength={18}
            />
          </div>
        </motion.div>

        <motion.div variants={itemVariants} className="space-y-2 col-span-2">
          <Label htmlFor="email">Email</Label>
          <div className="relative">
            <Mail className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
            <Input
              id="email"
              type="email"
              placeholder="cliente@email.com"
              value={data.email || ''}
              onChange={(e) => onChange({ ...data, email: e.target.value })}
              className="h-12 pl-10"
            />
          </div>
        </motion.div>
      </div>
    </motion.div>
  );
}
