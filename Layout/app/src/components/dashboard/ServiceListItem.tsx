import { motion } from 'framer-motion';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';
import { cn } from '@/lib/utils';
import type { Service } from '@/types';
import { StatusBadge } from '@/components/shared/StatusBadge';

interface ServiceListItemProps {
  service: Service;
  isSelected: boolean;
  onClick: () => void;
  index: number;
}

export function ServiceListItem({
  service,
  isSelected,
  onClick,
  index,
}: ServiceListItemProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{
        duration: 0.3,
        delay: 0.2 + index * 0.05,
        ease: [0.16, 1, 0.3, 1],
      }}
      whileHover={{ scale: 1.02 }}
      whileTap={{ scale: 0.98 }}
      onClick={onClick}
      className={cn(
        'relative p-4 rounded-xl cursor-pointer transition-all duration-200 border-2',
        isSelected
          ? 'bg-blue-50 border-blue-500 shadow-lg'
          : 'bg-white border-transparent shadow-md hover:shadow-lg hover:border-gray-200'
      )}
    >
      {/* Left border indicator */}
      {isSelected && (
        <motion.div
          layoutId="listItemIndicator"
          className="absolute left-0 top-1/2 -translate-y-1/2 w-1 h-12 bg-blue-500 rounded-r-full"
          transition={{ type: 'spring', stiffness: 500, damping: 30 }}
        />
      )}

      <div className="flex items-start justify-between mb-2">
        <h3
          className={cn(
            'text-2xl font-bold tracking-wide',
            isSelected ? 'text-blue-900' : 'text-gray-800'
          )}
        >
          {service.plate}
        </h3>
        <StatusBadge status={service.status} size="sm" />
      </div>

      <p className="text-gray-600 font-medium mb-1">{service.client.name}</p>

      <div className="flex items-center justify-between mt-3">
        <p className="text-sm text-gray-400">
          {service.vehicle.brand} {service.vehicle.model}
        </p>
        <p className="text-xs text-gray-400">
          {format(new Date(service.entryDate), "dd/MM/yyyy 'às' HH:mm", {
            locale: ptBR,
          })}
        </p>
      </div>
    </motion.div>
  );
}
