import { useRef, useState } from 'react';
import { motion } from 'framer-motion';
import { PenTool, Eraser, Check, AlertTriangle } from 'lucide-react';
import { Button } from '@/components/ui/button';
import SignatureCanvas from 'react-signature-canvas';

interface SignatureStepProps {
  onSign: (signature: string) => void;
}

export function SignatureStep({ onSign }: SignatureStepProps) {
  const signatureRef = useRef<SignatureCanvas>(null);
  const [isEmpty, setIsEmpty] = useState(true);
  const [showError, setShowError] = useState(false);

  const handleClear = () => {
    signatureRef.current?.clear();
    setIsEmpty(true);
    setShowError(false);
  };

  const handleConfirm = () => {
    if (signatureRef.current?.isEmpty()) {
      setShowError(true);
      return;
    }

    const signatureData = signatureRef.current?.toDataURL('image/png');
    if (signatureData) {
      onSign(signatureData);
    }
  };

  const handleBegin = () => {
    setIsEmpty(false);
    setShowError(false);
  };

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      className="space-y-6"
    >
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="text-center mb-4"
      >
        <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <PenTool className="w-8 h-8 text-blue-600" />
        </div>
        <h2 className="text-2xl font-bold text-gray-900">Assinatura do Cliente</h2>
        <p className="text-gray-500">
          Por favor, peça ao cliente para assinar na área abaixo
        </p>
      </motion.div>

      <motion.div
        initial={{ opacity: 0, scale: 0.95 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ delay: 0.2 }}
        className="relative"
      >
        <div
          className={`relative bg-white rounded-xl border-2 border-dashed transition-colors ${
            showError ? 'border-red-400 bg-red-50' : 'border-gray-300'
          }`}
        >
          <SignatureCanvas
            ref={signatureRef}
            canvasProps={{
              className: 'w-full h-64 cursor-crosshair rounded-xl',
              style: { width: '100%', height: '256px' },
            }}
            backgroundColor="rgba(255, 255, 255, 0)"
            onBegin={handleBegin}
          />

          {isEmpty && (
            <div className="absolute inset-0 flex items-center justify-center pointer-events-none">
              <span className="text-gray-300 text-lg font-medium">
                Assine aqui
              </span>
            </div>
          )}
        </div>

        {showError && (
          <motion.div
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            className="flex items-center gap-2 text-red-500 mt-2 text-sm"
          >
            <AlertTriangle className="w-4 h-4" />
            É necessário uma assinatura para continuar
          </motion.div>
        )}
      </motion.div>

      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.4 }}
        className="flex justify-center gap-4"
      >
        <Button
          type="button"
          variant="outline"
          size="lg"
          onClick={handleClear}
          className="flex items-center gap-2"
        >
          <Eraser className="w-5 h-5" />
          Limpar
        </Button>
        <Button
          type="button"
          size="lg"
          onClick={handleConfirm}
          className="flex items-center gap-2 bg-green-600 hover:bg-green-700"
        >
          <Check className="w-5 h-5" />
          Confirmar Check-in
        </Button>
      </motion.div>

      <motion.p
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        transition={{ delay: 0.5 }}
        className="text-center text-sm text-gray-400"
      >
        Ao assinar, o cliente confirma que leu e concorda com os termos do serviço
      </motion.p>
    </motion.div>
  );
}
