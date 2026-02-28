import { motion } from 'framer-motion';
import { ClipboardList, History, Settings, Car } from 'lucide-react';
import { cn } from '@/lib/utils';

interface SidebarProps {
  activeItem: string;
  onItemClick: (item: string) => void;
}

const menuItems = [
  { id: 'dashboard', label: 'Atendimentos', icon: ClipboardList },
  { id: 'history', label: 'Histórico', icon: History },
  { id: 'settings', label: 'Configurações', icon: Settings },
];

export function Sidebar({ activeItem, onItemClick }: SidebarProps) {
  return (
    <motion.aside
      initial={{ x: -100, opacity: 0 }}
      animate={{ x: 0, opacity: 1 }}
      transition={{ duration: 0.5, ease: [0.16, 1, 0.3, 1] }}
      className="fixed left-0 top-0 h-full w-20 bg-[#1a365d] flex flex-col items-center py-6 z-50 shadow-xl"
    >
      {/* Logo */}
      <motion.div
        initial={{ scale: 0.8, opacity: 0 }}
        animate={{ scale: 1, opacity: 1 }}
        transition={{ duration: 0.4, delay: 0.2, ease: 'backOut' }}
        className="mb-10"
      >
        <div className="w-12 h-12 bg-white rounded-xl flex items-center justify-center shadow-lg">
          <Car className="w-7 h-7 text-[#1a365d]" />
        </div>
      </motion.div>

      {/* Menu Items */}
      <nav className="flex flex-col gap-3 w-full px-2">
        {menuItems.map((item, index) => {
          const Icon = item.icon;
          const isActive = activeItem === item.id;

          return (
            <motion.button
              key={item.id}
              initial={{ x: -20, opacity: 0 }}
              animate={{ x: 0, opacity: 1 }}
              transition={{
                duration: 0.3,
                delay: 0.25 + index * 0.05,
                ease: [0.16, 1, 0.3, 1],
              }}
              onClick={() => onItemClick(item.id)}
              className={cn(
                'relative flex flex-col items-center justify-center w-full py-4 rounded-xl transition-all duration-200 group',
                isActive
                  ? 'bg-[#2c5282] text-white'
                  : 'text-gray-400 hover:bg-[#2c5282]/50 hover:text-white'
              )}
            >
              {/* Active indicator */}
              {isActive && (
                <motion.div
                  layoutId="activeIndicator"
                  className="absolute left-0 top-1/2 -translate-y-1/2 w-1 h-8 bg-[#3182ce] rounded-r-full"
                  transition={{ type: 'spring', stiffness: 500, damping: 30 }}
                />
              )}

              <motion.div
                whileHover={{ scale: 1.1 }}
                whileTap={{ scale: 0.95 }}
                className="relative"
              >
                <Icon className="w-6 h-6" />
              </motion.div>

              <span className="text-[10px] mt-1 font-medium">{item.label}</span>
            </motion.button>
          );
        })}
      </nav>

      {/* Version */}
      <div className="mt-auto">
        <span className="text-[10px] text-gray-500">v1.0</span>
      </div>
    </motion.aside>
  );
}
