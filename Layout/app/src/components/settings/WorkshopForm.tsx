import { motion } from 'framer-motion';
import { Building2, MapPin, Phone, FileText, Upload } from 'lucide-react';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import type { WorkshopSettings } from '@/types';

interface WorkshopFormProps {
  settings: WorkshopSettings;
  onChange: (settings: Partial<WorkshopSettings>) => void;
}

export function WorkshopForm({ settings, onChange }: WorkshopFormProps) {
  const handleLogoUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        onChange({ workshopLogo: reader.result as string });
      };
      reader.readAsDataURL(file);
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: 0.1 }}
    >
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Building2 className="w-5 h-5 text-blue-600" />
            Informações da Oficina
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-6">
          {/* Logo Upload */}
          <div className="flex items-center gap-6">
            <div className="w-24 h-24 bg-gray-100 rounded-xl flex items-center justify-center overflow-hidden">
              {settings.workshopLogo ? (
                <img
                  src={settings.workshopLogo}
                  alt="Logo"
                  className="w-full h-full object-contain"
                />
              ) : (
                <Building2 className="w-10 h-10 text-gray-400" />
              )}
            </div>
            <div>
              <Label htmlFor="logo" className="block mb-2">
                Logo da Oficina
              </Label>
              <div className="flex items-center gap-2">
                <Input
                  id="logo"
                  type="file"
                  accept="image/*"
                  onChange={handleLogoUpload}
                  className="hidden"
                />
                <Button
                  variant="outline"
                  onClick={() => document.getElementById('logo')?.click()}
                >
                  <Upload className="w-4 h-4 mr-2" />
                  Upload
                </Button>
                {settings.workshopLogo && (
                  <Button
                    variant="ghost"
                    onClick={() => onChange({ workshopLogo: undefined })}
                  >
                    Remover
                  </Button>
                )}
              </div>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="workshopName">Nome da Oficina *</Label>
              <Input
                id="workshopName"
                value={settings.workshopName}
                onChange={(e) => onChange({ workshopName: e.target.value })}
                placeholder="Ex: Oficina AutoCheck Pro"
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="cnpj">CNPJ</Label>
              <div className="relative">
                <FileText className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                <Input
                  id="cnpj"
                  value={settings.cnpj}
                  onChange={(e) => onChange({ cnpj: e.target.value })}
                  placeholder="00.000.000/0000-00"
                  className="pl-10"
                />
              </div>
            </div>

            <div className="space-y-2 col-span-2">
              <Label htmlFor="address">Endereço</Label>
              <div className="relative">
                <MapPin className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                <Input
                  id="address"
                  value={settings.address}
                  onChange={(e) => onChange({ address: e.target.value })}
                  placeholder="Rua, número, bairro, cidade/UF"
                  className="pl-10"
                />
              </div>
            </div>

            <div className="space-y-2">
              <Label htmlFor="phone">Telefone</Label>
              <div className="relative">
                <Phone className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                <Input
                  id="phone"
                  value={settings.phone}
                  onChange={(e) => onChange({ phone: e.target.value })}
                  placeholder="(11) 3333-4444"
                  className="pl-10"
                />
              </div>
            </div>
          </div>
        </CardContent>
      </Card>
    </motion.div>
  );
}
