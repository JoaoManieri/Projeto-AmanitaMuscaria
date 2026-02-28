import { motion } from 'framer-motion';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';
import { Eye, Printer, Download, FileText } from 'lucide-react';
import { Button } from '@/components/ui/button';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import type { Service } from '@/types';
import { StatusBadge } from '@/components/shared/StatusBadge';

interface HistoryTableProps {
  services: Service[];
  onView: (service: Service) => void;
  onPrint: (service: Service) => void;
  onDownload: (service: Service) => void;
}

export function HistoryTable({
  services,
  onView,
  onPrint,
  onDownload,
}: HistoryTableProps) {
  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: { staggerChildren: 0.05 },
    },
  };

  const rowVariants = {
    hidden: { opacity: 0, y: 10 },
    visible: { opacity: 1, y: 0 },
  };

  return (
    <motion.div
      variants={containerVariants}
      initial="hidden"
      animate="visible"
      className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden"
    >
      <Table>
        <TableHeader>
          <TableRow className="bg-gray-50">
            <TableHead className="font-semibold">Data</TableHead>
            <TableHead className="font-semibold">Placa</TableHead>
            <TableHead className="font-semibold">Cliente</TableHead>
            <TableHead className="font-semibold">Veículo</TableHead>
            <TableHead className="font-semibold">Status</TableHead>
            <TableHead className="font-semibold text-right">Ações</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {services.length === 0 ? (
            <TableRow>
              <TableCell colSpan={6} className="text-center py-12">
                <FileText className="w-12 h-12 text-gray-300 mx-auto mb-3" />
                <p className="text-gray-500">Nenhum atendimento encontrado</p>
                <p className="text-sm text-gray-400">
                  Tente ajustar os filtros de busca
                </p>
              </TableCell>
            </TableRow>
          ) : (
            services.map((service) => (
              <motion.tr
                key={service.id}
                variants={rowVariants}
                className="border-b hover:bg-gray-50 transition-colors group"
              >
                <TableCell>
                  {format(new Date(service.entryDate), 'dd/MM/yyyy', {
                    locale: ptBR,
                  })}
                </TableCell>
                <TableCell className="font-semibold text-lg">
                  {service.plate}
                </TableCell>
                <TableCell>{service.client.name}</TableCell>
                <TableCell>
                  {service.vehicle.brand} {service.vehicle.model}
                </TableCell>
                <TableCell>
                  <StatusBadge status={service.status} size="sm" />
                </TableCell>
                <TableCell className="text-right">
                  <div className="flex items-center justify-end gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => onView(service)}
                    >
                      <Eye className="w-4 h-4" />
                    </Button>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => onPrint(service)}
                    >
                      <Printer className="w-4 h-4" />
                    </Button>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => onDownload(service)}
                    >
                      <Download className="w-4 h-4" />
                    </Button>
                  </div>
                </TableCell>
              </motion.tr>
            ))
          )}
        </TableBody>
      </Table>
    </motion.div>
  );
}
