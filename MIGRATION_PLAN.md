# MIGRATION_PLAN

## 1) Varredura do `./Layout` (React + Vite)

### Rotas/Telas React identificadas
| Tela React | Arquivo | Estado atual |
|---|---|---|
| Dashboard | `Layout/app/src/pages/Dashboard.tsx` | Lista de atendimentos + detalhe + wizard de check-in |
| History | `Layout/app/src/pages/History.tsx` | Filtros + cards de stats + tabela com ações |
| Settings | `Layout/app/src/pages/Settings.tsx` | Form da oficina + relatório + backup/restauração |

Observação: a navegação no React é interna por estado (`activePage` em `App.tsx`), sem `react-router-dom`.

### Componentes React principais identificados
| Componente React | Arquivo |
|---|---|
| Sidebar | `Layout/app/src/components/layout/Sidebar.tsx` |
| ServiceList | `Layout/app/src/components/dashboard/ServiceList.tsx` |
| ServiceListItem | `Layout/app/src/components/dashboard/ServiceListItem.tsx` |
| ServiceDetails | `Layout/app/src/components/dashboard/ServiceDetails.tsx` |
| EmptyState | `Layout/app/src/components/dashboard/EmptyState.tsx` |
| CheckinWizard | `Layout/app/src/components/checkin/CheckinWizard.tsx` |
| StepIndicator | `Layout/app/src/components/checkin/StepIndicator.tsx` |
| VehicleStep | `Layout/app/src/components/checkin/VehicleStep.tsx` |
| ClientStep | `Layout/app/src/components/checkin/ClientStep.tsx` |
| InspectionStep | `Layout/app/src/components/checkin/InspectionStep.tsx` |
| SignatureStep | `Layout/app/src/components/checkin/SignatureStep.tsx` |
| FilterBar | `Layout/app/src/components/history/FilterBar.tsx` |
| HistoryTable | `Layout/app/src/components/history/HistoryTable.tsx` |
| WorkshopForm | `Layout/app/src/components/settings/WorkshopForm.tsx` |
| StatusBadge | `Layout/app/src/components/shared/StatusBadge.tsx` |

### Estilos/Tema identificados
- Base CSS: `Layout/app/src/index.css`
- Tokens Tailwind: `Layout/app/tailwind.config.js`
- Design-base: shadcn style `new-york` + Radix + Tailwind variables
- Layout global: sidebar fixa `w-20` e conteúdo com `ml-20`
- Animações: Framer Motion (transições de página, fade/slide, hover/tap), keyframes CSS (pulse/shimmer)

### Assets identificados
| Asset | Local |
|---|---|
| `logo.png` | `Layout/assets/logo.png`, `Layout/app/public/logo.png` |
| `empty-state.png` | `Layout/assets/empty-state.png`, `Layout/app/public/empty-state.png` |
| `car-diagram.png` | `Layout/assets/car-diagram.png`, `Layout/app/public/car-diagram.png` |

### Dependências React detectadas (`Layout/app/package.json`)
- Core: `react`, `react-dom`, `vite`, `typescript`
- UI/estilo: `tailwindcss`, `class-variance-authority`, `clsx`, `tailwind-merge`, Radix UI
- Ícones/animação: `lucide-react`, `framer-motion`
- Estado: `zustand`
- Form/validação: `react-hook-form`, `zod`, `@hookform/resolvers`
- Especiais: `react-signature-canvas`, `react-webcam`, `recharts`, `sonner`, `date-fns`

---

## 2) Mapeamento 1:1 React -> KMP Compose

### Tabela Tela React -> Composable KMP
| Tela React | Composable KMP (destino) | Etapa |
|---|---|---|
| Dashboard | `DashboardScreen` | B |
| Check-in Wizard (subfluxo da Dashboard) | `CheckinWizardScreen` + steps composables | B/C |
| History | `HistoryScreen` | C |
| Settings | `SettingsScreen` | C |

### Tabela Componente React -> Composable KMP
| Componente React | Composable KMP (destino) | Etapa |
|---|---|---|
| Sidebar | `AppSidebar` | A |
| ServiceList | `ServiceListPane` | B |
| ServiceListItem | `ServiceListItemCard` | B |
| ServiceDetails | `ServiceDetailsPane` | B |
| EmptyState | `ServiceEmptyState` | B |
| StatusBadge | `StatusBadge` | B |
| CheckinWizard | `CheckinWizardScreen` | B |
| StepIndicator | `CheckinStepIndicator` | B |
| VehicleStep | `VehicleStepForm` | B |
| ClientStep | `ClientStepForm` | B |
| InspectionStep | `InspectionStepForm` | C |
| SignatureStep | `SignatureStepForm` | C |
| FilterBar | `HistoryFilterBar` | C |
| HistoryTable | `HistoryDataTable` | C |
| WorkshopForm | `WorkshopFormCard` | C |

### Tokens de design (base a portar)
| Categoria | Tokens principais |
|---|---|
| Cores | `#1A365D`, `#2C5282`, `#3182CE`, `#E2E8F0`, `#F7FAFC`, `#1A202C`, `#718096`, `#38A169`, `#DD6B20` |
| Spacing | `4, 6, 8, 10, 12, 14, 16, 20, 24, 32` |
| Radius | `10px` (md), `12px` (lg) |
| Tipografia | Base Inter/Sans; H1 `32/700`, H2 `24/700`, H3 `20/600`, body `16/400`, small `14/400`, caption `12/500` |
| Layout tablet | Sidebar fixa `80px`, conteúdo offset `80px`, prioridade landscape |

---

## 3) Bibliotecas KMP a adicionar

| Lib | Motivo |
|---|---|
| `org.jetbrains.compose.material:material-icons-extended` | Ícones equivalentes ao `lucide-react` no shell/base |
| Target `desktop` (Compose Desktop) | Execução e validação parity também em Desktop |
| `io.ktor:ktor-client-*` + `kotlinx.serialization` | Somente se existir fluxo de rede real nas próximas etapas (no app React atual não há fetch/axios ativo) |

---

## 4) Etapas de migração (incremental e compilável)

### Etapa A (em andamento)
- Base KMP + Theme + Design tokens
- Navegação base `dashboard/history/settings`
- Layout shell com sidebar fixa e transição entre telas

### Etapa B
- Dashboard 1:1
- Componentes base compartilhados (button/card/input/select/status-badge)
- Fluxo principal do check-in (passos de veículo/cliente)

### Etapa C
- History e Settings 1:1
- Inspeção (hotspots/fotos) e assinatura
- Estados completos (hover/pressed/focus aplicáveis no desktop/tablet)

### Etapa D
- Assets finais (logo, car diagram, empty state, fontes)
- Polimento visual (paridade pixel/spacing/typography)
- Parity check final + ajustes finos

---

## 5) Status atual da execução
- `MIGRATION_PLAN.md` criado.
- Etapa A iniciada no projeto KMP:
  - theme/tokens base implementados,
  - sidebar base implementada,
  - navegação com animação entre páginas implementada,
  - target desktop habilitado para execução local.
- Etapa B iniciada no projeto KMP:
  - `DashboardScreen` com layout 35/65 equivalente (lista + detalhe),
  - estado compartilhado com dados mockados equivalente ao store React,
  - `CheckinWizardScreen` funcional de 4 passos para criar atendimento,
  - `StatusBadge` e cards de detalhes base implementados.
- Etapa C iniciada no projeto KMP:
  - `HistoryScreen` implementada com filtros, stats e tabela de acoes,
  - `SettingsScreen` implementada com formulario da oficina, toggles e bloco de backup,
  - wizard evoluido com inspeção por regioes e assinatura em canvas.
- Etapa D iniciada no projeto KMP:
  - assets reais adicionados em `composeResources/drawable` (`logo`, `empty_state`, `car_diagram`),
  - sidebar usando logo real e empty-state com imagem real,
  - inspeção visual usando `car_diagram` com hotspots sobrepostos.
