import { motion } from 'framer-motion';
import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import type { Service } from '@/types';
import { ServiceListItem } from './ServiceListItem';
import { ScrollArea } from '@/components/ui/scroll-area';

interface ServiceListProps {
  services: Service[];
  selectedId: string | null;
  onSelect: (id: string) => void;
  onNewCheckin: () => void;
}

export function ServiceList({
  services,
  selectedId,
  onSelect,
  onNewCheckin,
}: ServiceListProps) {
  return (
    <motion.div
      initial={{ opacity: 0, x: -50 }}
      animate={{ opacity: 1, x: 0 }}
      transition={{ duration: 0.4, delay: 0.1, ease: [0.16, 1, 0.3, 1] }}
      className="h-full flex flex-col bg-gray-50/50"
    >
      {/* Header */}
      <div className="p-6 border-b border-gray-200 bg-white">
        <motion.h2
          initial={{ opacity: 0, y: -10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
          className="text-xl font-bold text-gray-800 mb-4"
        >
          Atendimentos em Aberto
        </motion.h2>

        <motion.div
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3 }}
        >
          <Button
            onClick={onNewCheckin}
            className="w-full h-14 text-lg font-semibold bg-[#3182ce] hover:bg-[#2c5282] transition-all duration-200 shadow-lg hover:shadow-xl"
          >
            <Plus className="w-5 h-5 mr-2" />
            Novo Check-in
          </Button>
        </motion.div>
      </div>

      {/* List */}
      <ScrollArea className="flex-1 p-4">
        <div className="space-y-3">
          {services.length === 0 ? (
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="text-center py-8 text-gray-500"
            >
              <p>Nenhum atendimento em aberto</p>
              <p className="text-sm mt-1">Clique em "Novo Check-in" para começar</p>
            </motion.div>
          ) : (
            services.map((service, index) => (
              <ServiceListItem
                key={service.id}
                service={service}
                isSelected={selectedId === service.id}
                onClick={() => onSelect(service.id)}
                index={index}
              />
            ))
          )}
        </div>
      </ScrollArea>

      {/* Footer stats */}
      <div className="p-4 border-t border-gray-200 bg-white">
        <div className="flex justify-between text-sm text-gray-500">
          <span>Total: {services.length}</span>
          <span>
            Em andamento: {services.filter((s) => s.status === 'in_progress').length}
          </span>
        </div>
      </div>
    </motion.div>
  );
}
