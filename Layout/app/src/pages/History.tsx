import { useState, useMemo } from 'react';
import { motion } from 'framer-motion';
import { History } from 'lucide-react';
import { useServiceStore } from '@/store';
import type { Service } from '@/types';
import { FilterBar } from '@/components/history/FilterBar';
import { HistoryTable } from '@/components/history/HistoryTable';
import { toast } from 'sonner';

export function HistoryPage() {
  const { services } = useServiceStore();
  const [filters, setFilters] = useState({
    plate: '',
    client: '',
    status: 'all',
  });

  const filteredServices = useMemo(() => {
    return services.filter((service) => {
      const matchesPlate =
        !filters.plate ||
        service.plate.toLowerCase().includes(filters.plate.toLowerCase());
      const matchesClient =
        !filters.client ||
        service.client.name.toLowerCase().includes(filters.client.toLowerCase());
      const matchesStatus =
        filters.status === 'all' || service.status === filters.status;

      return matchesPlate && matchesClient && matchesStatus;
    });
  }, [services, filters]);

  const handleView = (service: Service) => {
    toast.info('Visualizando atendimento', {
      description: `Veículo: ${service.plate}`,
    });
  };

  const handlePrint = (service: Service) => {
    toast.info('Preparando impressão...', {
      description: `Relatório do veículo ${service.plate}`,
    });
  };

  const handleDownload = (service: Service) => {
    toast.success('Download iniciado', {
      description: `Relatório_${service.plate}.pdf`,
    });
  };

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      className="h-full flex flex-col p-6"
    >
      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        className="mb-6"
      >
        <div className="flex items-center gap-3 mb-2">
          <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center">
            <History className="w-5 h-5 text-blue-600" />
          </div>
          <h1 className="text-2xl font-bold text-gray-900">Histórico de Atendimentos</h1>
        </div>
        <p className="text-gray-500 ml-13">
          Visualize e gerencie todos os atendimentos realizados
        </p>
      </motion.div>

      {/* Filters */}
      <FilterBar
        filters={filters}
        onFilterChange={setFilters}
        onSearch={() => {
          toast.success('Filtros aplicados');
        }}
      />

      {/* Stats */}
      <motion.div
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.2 }}
        className="grid grid-cols-4 gap-4 mb-6"
      >
        <div className="bg-white p-4 rounded-xl shadow-sm border border-gray-200">
          <p className="text-sm text-gray-500">Total</p>
          <p className="text-2xl font-bold text-gray-900">{services.length}</p>
        </div>
        <div className="bg-white p-4 rounded-xl shadow-sm border border-gray-200">
          <p className="text-sm text-gray-500">Em andamento</p>
          <p className="text-2xl font-bold text-blue-600">
            {services.filter((s) => s.status === 'in_progress').length}
          </p>
        </div>
        <div className="bg-white p-4 rounded-xl shadow-sm border border-gray-200">
          <p className="text-sm text-gray-500">Aguardando</p>
          <p className="text-2xl font-bold text-orange-500">
            {services.filter((s) => s.status === 'waiting_pickup').length}
          </p>
        </div>
        <div className="bg-white p-4 rounded-xl shadow-sm border border-gray-200">
          <p className="text-sm text-gray-500">Finalizados</p>
          <p className="text-2xl font-bold text-green-600">
            {services.filter((s) => s.status === 'completed').length}
          </p>
        </div>
      </motion.div>

      {/* Table */}
      <div className="flex-1 overflow-auto">
        <HistoryTable
          services={filteredServices}
          onView={handleView}
          onPrint={handlePrint}
          onDownload={handleDownload}
        />
      </div>
    </motion.div>
  );
}
