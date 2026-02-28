import { motion } from 'framer-motion';
import { Search, Filter } from 'lucide-react';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';

interface FilterBarProps {
  filters: {
    plate: string;
    client: string;
    status: string;
  };
  onFilterChange: (filters: { plate: string; client: string; status: string }) => void;
  onSearch: () => void;
}

export function FilterBar({ filters, onFilterChange, onSearch }: FilterBarProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: -20 }}
      animate={{ opacity: 1, y: 0 }}
      className="bg-white p-4 rounded-xl shadow-sm border border-gray-200 mb-6"
    >
      <div className="flex items-center gap-4">
        <div className="flex-1">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
            <Input
              placeholder="Buscar por placa..."
              value={filters.plate}
              onChange={(e) => onFilterChange({ ...filters, plate: e.target.value })}
              className="pl-10"
            />
          </div>
        </div>

        <div className="flex-1">
          <div className="relative">
            <Filter className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
            <Input
              placeholder="Buscar por cliente..."
              value={filters.client}
              onChange={(e) => onFilterChange({ ...filters, client: e.target.value })}
              className="pl-10"
            />
          </div>
        </div>

        <div className="w-48">
          <Select
            value={filters.status}
            onValueChange={(value) => onFilterChange({ ...filters, status: value })}
          >
            <SelectTrigger>
              <SelectValue placeholder="Status" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">Todos</SelectItem>
              <SelectItem value="in_progress">Em andamento</SelectItem>
              <SelectItem value="waiting_pickup">Aguardando retirada</SelectItem>
              <SelectItem value="completed">Finalizado</SelectItem>
            </SelectContent>
          </Select>
        </div>

        <Button onClick={onSearch} className="bg-blue-600 hover:bg-blue-700">
          <Search className="w-4 h-4 mr-2" />
          Buscar
        </Button>
      </div>
    </motion.div>
  );
}
