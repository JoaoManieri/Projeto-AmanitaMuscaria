import { motion } from 'framer-motion';
import { cn } from '@/lib/utils';
import type { ServiceStatus } from '@/types';

interface StatusBadgeProps {
  status: ServiceStatus;
  size?: 'sm' | 'md' | 'lg';
}

const statusConfig = {
  in_progress: {
    label: 'Em andamento',
    bgColor: 'bg-blue-100',
    textColor: 'text-blue-700',
    borderColor: 'border-blue-200',
    pulse: true,
  },
  waiting_pickup: {
    label: 'Aguardando retirada',
    bgColor: 'bg-orange-100',
    textColor: 'text-orange-700',
    borderColor: 'border-orange-200',
    pulse: true,
  },
  completed: {
    label: 'Finalizado',
    bgColor: 'bg-green-100',
    textColor: 'text-green-700',
    borderColor: 'border-green-200',
    pulse: false,
  },
};

const sizeConfig = {
  sm: 'text-xs px-2 py-0.5',
  md: 'text-sm px-3 py-1',
  lg: 'text-base px-4 py-1.5',
};

export function StatusBadge({ status, size = 'md' }: StatusBadgeProps) {
  const config = statusConfig[status];

  return (
    <motion.span
      initial={{ scale: 0.8, opacity: 0 }}
      animate={{ scale: 1, opacity: 1 }}
      className={cn(
        'inline-flex items-center gap-1.5 font-medium rounded-full border',
        config.bgColor,
        config.textColor,
        config.borderColor,
        sizeConfig[size]
      )}
    >
      {config.pulse && (
        <span className="relative flex h-2 w-2">
          <span
            className={cn(
              'animate-ping absolute inline-flex h-full w-full rounded-full opacity-75',
              status === 'in_progress' ? 'bg-blue-400' : 'bg-orange-400'
            )}
          />
          <span
            className={cn(
              'relative inline-flex rounded-full h-2 w-2',
              status === 'in_progress' ? 'bg-blue-500' : 'bg-orange-500'
            )}
          />
        </span>
      )}
      {!config.pulse && (
        <span className="w-2 h-2 rounded-full bg-green-500" />
      )}
      {config.label}
    </motion.span>
  );
}
