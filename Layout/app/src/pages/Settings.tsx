import { motion } from 'framer-motion';
import { Settings, Save, Download, Upload, FileText } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Label } from '@/components/ui/label';
import { Input } from '@/components/ui/input';
import { Switch } from '@/components/ui/switch';
import { useSettingsStore } from '@/store';
import { WorkshopForm } from '@/components/settings/WorkshopForm';
import { toast } from 'sonner';

export function SettingsPage() {
  const { settings, updateSettings } = useSettingsStore();

  const handleSave = () => {
    toast.success('Configurações salvas com sucesso!');
  };

  const handleExport = () => {
    const data = JSON.stringify({ services: [], settings }, null, 2);
    const blob = new Blob([data], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `autocheck-backup-${new Date().toISOString().split('T')[0]}.json`;
    a.click();
    URL.revokeObjectURL(url);

    toast.success('Backup exportado com sucesso!');
  };

  const handleImport = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (event) => {
        try {
          const data = JSON.parse(event.target?.result as string);
          if (data.settings) {
            updateSettings(data.settings);
            toast.success('Backup importado com sucesso!');
          }
        } catch {
          toast.error('Erro ao importar backup. Arquivo inválido.');
        }
      };
      reader.readAsText(file);
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      className="h-full overflow-auto p-6"
    >
      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex items-center justify-between mb-6"
      >
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center">
            <Settings className="w-5 h-5 text-blue-600" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Configurações</h1>
            <p className="text-gray-500">Personalize o sistema da sua oficina</p>
          </div>
        </div>

        <Button onClick={handleSave} className="bg-blue-600 hover:bg-blue-700">
          <Save className="w-4 h-4 mr-2" />
          Salvar Alterações
        </Button>
      </motion.div>

      <div className="max-w-4xl space-y-6">
        {/* Workshop Info */}
        <WorkshopForm settings={settings} onChange={updateSettings} />

        {/* Report Settings */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
        >
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <FileText className="w-5 h-5 text-blue-600" />
                Configurações de Relatório
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="space-y-2">
                <Label htmlFor="reportHeader">Cabeçalho do Relatório</Label>
                <Input
                  id="reportHeader"
                  value={settings.reportHeader}
                  onChange={(e) => updateSettings({ reportHeader: e.target.value })}
                  placeholder="Ex: Relatório de Serviço"
                />
              </div>

              <div className="flex items-center justify-between py-2">
                <div>
                  <p className="font-medium">Mostrar logo nos relatórios</p>
                  <p className="text-sm text-gray-500">
                    Inclui o logo da oficina nos relatórios impressos
                  </p>
                </div>
                <Switch
                  checked={settings.showLogoInReport}
                  onCheckedChange={(checked) =>
                    updateSettings({ showLogoInReport: checked })
                  }
                />
              </div>

              <div className="flex items-center justify-between py-2">
                <div>
                  <p className="font-medium">Assinatura obrigatória</p>
                  <p className="text-sm text-gray-500">
                    Exige assinatura do cliente no check-out
                  </p>
                </div>
                <Switch
                  checked={settings.requireSignature}
                  onCheckedChange={(checked) =>
                    updateSettings({ requireSignature: checked })
                  }
                />
              </div>
            </CardContent>
          </Card>
        </motion.div>

        {/* Backup */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.3 }}
        >
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <Download className="w-5 h-5 text-blue-600" />
                Backup e Restauração
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="font-medium">Exportar dados</p>
                  <p className="text-sm text-gray-500">
                    Faça backup de todos os atendimentos e configurações
                  </p>
                </div>
                <Button variant="outline" onClick={handleExport}>
                  <Download className="w-4 h-4 mr-2" />
                  Exportar
                </Button>
              </div>

              <div className="flex items-center justify-between">
                <div>
                  <p className="font-medium">Importar dados</p>
                  <p className="text-sm text-gray-500">
                    Restaure dados de um backup anterior
                  </p>
                </div>
                <div>
                  <Input
                    type="file"
                    accept=".json"
                    onChange={handleImport}
                    className="hidden"
                    id="import-file"
                  />
                  <Button
                    variant="outline"
                    onClick={() => document.getElementById('import-file')?.click()}
                  >
                    <Upload className="w-4 h-4 mr-2" />
                    Importar
                  </Button>
                </div>
              </div>

              <div className="bg-gray-50 p-4 rounded-lg">
                <p className="text-sm text-gray-500">
                  <strong>Último backup:</strong>{' '}
                  {new Date().toLocaleDateString('pt-BR', {
                    day: '2-digit',
                    month: '2-digit',
                    year: 'numeric',
                    hour: '2-digit',
                    minute: '2-digit',
                  })}
                </p>
              </div>
            </CardContent>
          </Card>
        </motion.div>

        {/* About */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.4 }}
        >
          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="font-medium">AutoCheck Pro</p>
                  <p className="text-sm text-gray-500">Versão 1.0.0</p>
                </div>
                <div className="text-right">
                  <p className="text-sm text-gray-500">
                    Sistema de Gestão para Oficinas Mecânicas
                  </p>
                  <p className="text-sm text-gray-400">
                    © 2024 AutoCheck Pro. Todos os direitos reservados.
                  </p>
                </div>
              </div>
            </CardContent>
          </Card>
        </motion.div>
      </div>
    </motion.div>
  );
}
