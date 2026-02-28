# AutoCheck Pro - Technical Specification

## Component Inventory

### shadcn/ui Components (Built-in)
| Component | Purpose | Customization |
|-----------|---------|---------------|
| Button | All actions | Size variants for tablet touch |
| Card | Content containers | Custom shadows, borders |
| Input | Form fields | Larger touch targets |
| Label | Form labels | Floating label animation |
| Select | Dropdowns | Custom styling |
| Badge | Status indicators | Color variants |
| Avatar | Client photos | Fallback initials |
| Dialog | Modals | Full-screen on tablet |
| Tabs | Content switching | Animated indicator |
| Separator | Visual dividers | Custom colors |
| ScrollArea | Custom scrollbars | Hidden scrollbars |
| Skeleton | Loading states | Shimmer animation |
| Tooltip | Help text | Delayed show |
| Toast | Notifications | Bottom-right position |
| Form | Form validation | React Hook Form integration |

### Third-party Components
| Component | Source | Purpose |
|-----------|--------|---------|
| react-signature-canvas | npm | Digital signature capture |
| react-webcam | npm | Photo capture for inspection |
| react-datepicker | npm | Date range selection |
| react-input-mask | npm | Input masking (plate, phone) |

### Custom Components
| Component | Purpose | Props |
|-----------|---------|-------|
| Sidebar | Navigation | activeItem, onItemClick |
| ServiceList | Left panel list | services, selectedId, onSelect |
| ServiceDetails | Right panel details | service, onCheckout, onReport |
| CheckinWizard | Multi-step form | onComplete, onCancel |
| CarDiagram | Interactive inspection | regions, onRegionClick, photos |
| PhotoGrid | Thumbnail display | photos, onDelete, onView |
| SignaturePad | Signature capture | onSign, onClear |
| StatusBadge | Status indicator | status (enum) |
| EmptyState | No selection view | icon, title, description |
| FilterBar | History filters | filters, onFilterChange |
| DataTable | History list | data, columns, onAction |

## Animation Implementation Table

| Animation | Library | Implementation Approach | Complexity |
|-----------|---------|------------------------|------------|
| Sidebar slide in | Framer Motion | `initial`, `animate` props | Low |
| Menu item stagger | Framer Motion | `staggerChildren` variant | Low |
| List item hover | CSS + Framer | `whileHover` prop | Low |
| Card selection | Framer Motion | `layoutId` for shared layout | Medium |
| Stepper progress | GSAP | Timeline animation | Medium |
| Form field stagger | Framer Motion | `staggerChildren` | Low |
| Car diagram draw | GSAP | SVG stroke-dashoffset | High |
| Hotspot pulse | CSS | Keyframe animation | Low |
| Photo capture flash | Framer Motion | AnimatePresence | Low |
| Signature stroke | Canvas API | Native implementation | Medium |
| Page transitions | Framer Motion | AnimatePresence + variants | Medium |
| Skeleton shimmer | CSS | Keyframe animation | Low |
| Status badge pulse | CSS | Keyframe animation | Low |
| Button press | Framer Motion | `whileTap` scale | Low |
| Modal/dialog | Framer Motion | AnimatePresence | Low |
| Table row stagger | Framer Motion | `staggerChildren` | Low |
| Toggle switch | Framer Motion | `layout` prop | Low |

## Animation Library Choices

### Primary: Framer Motion
- **Rationale**: Best React integration, declarative API, AnimatePresence for mount/unmount
- **Use for**: Component animations, gestures, layout animations, page transitions

### Secondary: GSAP
- **Rationale**: Complex timeline control, SVG animation, high-performance sequences
- **Use for**: Car diagram drawing, stepper progress, complex choreography

### Tertiary: CSS Animations
- **Rationale**: Simple, performant, no JS overhead
- **Use for**: Hover states, loading spinners, continuous pulses, shimmer effects

## Project File Structure

```
/mnt/okcomputer/output/app/
├── public/
│   ├── car-diagram.svg
│   ├── empty-state.svg
│   └── logo.svg
├── src/
│   ├── components/
│   │   ├── ui/                    # shadcn components
│   │   ├── layout/
│   │   │   ├── Sidebar.tsx
│   │   │   ├── MainLayout.tsx
│   │   │   └── PageHeader.tsx
│   │   ├── dashboard/
│   │   │   ├── ServiceList.tsx
│   │   │   ├── ServiceListItem.tsx
│   │   │   ├── ServiceDetails.tsx
│   │   │   ├── VehicleInfo.tsx
│   │   │   ├── ClientInfo.tsx
│   │   │   └── EmptyState.tsx
│   │   ├── checkin/
│   │   │   ├── CheckinWizard.tsx
│   │   │   ├── StepIndicator.tsx
│   │   │   ├── VehicleStep.tsx
│   │   │   ├── ClientStep.tsx
│   │   │   ├── InspectionStep.tsx
│   │   │   ├── CarDiagram.tsx
│   │   │   ├── PhotoGrid.tsx
│   │   │   └── SignatureStep.tsx
│   │   ├── history/
│   │   │   ├── FilterBar.tsx
│   │   │   ├── HistoryTable.tsx
│   │   │   └── HistoryCard.tsx
│   │   ├── settings/
│   │   │   ├── WorkshopForm.tsx
│   │   │   ├── ReportSettings.tsx
│   │   │   └── BackupSection.tsx
│   │   └── shared/
│   │       ├── StatusBadge.tsx
│   │       ├── AnimatedButton.tsx
│   │       └── LoadingSkeleton.tsx
│   ├── hooks/
│   │   ├── useServices.ts
│   │   ├── useLocalStorage.ts
│   │   ├── useInspection.ts
│   │   └── useSignature.ts
│   ├── types/
│   │   ├── index.ts
│   │   ├── service.ts
│   │   ├── vehicle.ts
│   │   └── client.ts
│   ├── store/
│   │   ├── index.ts
│   │   ├── serviceStore.ts
│   │   └── settingsStore.ts
│   ├── lib/
│   │   ├── utils.ts
│   │   ├── animations.ts
│   │   └── constants.ts
│   ├── pages/
│   │   ├── Dashboard.tsx
│   │   ├── NewCheckin.tsx
│   │   ├── History.tsx
│   │   └── Settings.tsx
│   ├── App.tsx
│   ├── main.tsx
│   └── index.css
├── index.html
├── package.json
├── tailwind.config.js
├── tsconfig.json
└── vite.config.ts
```

## Dependencies

### Core
```json
{
  "react": "^18.2.0",
  "react-dom": "^18.2.0",
  "react-router-dom": "^6.20.0"
}
```

### Animation
```json
{
  "framer-motion": "^10.16.0",
  "gsap": "^3.12.0",
  "@gsap/react": "^2.0.0"
}
```

### Forms & Validation
```json
{
  "react-hook-form": "^7.48.0",
  "zod": "^3.22.0",
  "@hookform/resolvers": "^3.3.0"
}
```

### UI Components
```json
{
  "@radix-ui/react-*": "latest",
  "class-variance-authority": "^0.7.0",
  "clsx": "^2.0.0",
  "tailwind-merge": "^2.0.0"
}
```

### Special Features
```json
{
  "react-signature-canvas": "^1.0.6",
  "react-webcam": "^7.2.0",
  "react-datepicker": "^4.21.0",
  "react-input-mask": "^2.0.4",
  "date-fns": "^2.30.0",
  "lucide-react": "^0.294.0"
}
```

### State Management
```json
{
  "zustand": "^4.4.0"
}
```

## Color Tokens (Tailwind Config)

```javascript
colors: {
  primary: {
    50: '#ebf8ff',
    100: '#bee3f8',
    200: '#90cdf4',
    300: '#63b3ed',
    400: '#4299e1',
    500: '#3182ce',  // Accent Blue
    600: '#2b6cb0',
    700: '#2c5282',  // Secondary Blue
    800: '#2a4365',
    900: '#1a365d',  // Primary Blue
  },
  success: {
    50: '#f0fff4',
    500: '#38a169',
    600: '#2f855a',
  },
  warning: {
    50: '#fffaf0',
    500: '#dd6b20',
    600: '#c05621',
  },
  danger: {
    50: '#fff5f5',
    500: '#e53e3e',
    600: '#c53030',
  },
  neutral: {
    50: '#f7fafc',
    100: '#edf2f7',
    200: '#e2e8f0',  // Light Gray
    300: '#cbd5e0',
    400: '#a0aec0',
    500: '#718096',
    600: '#4a5568',  // Neutral Gray
    700: '#2d3748',
    800: '#1a202c',
    900: '#171923',
  }
}
```

## Typography Scale

| Element | Size | Weight | Line Height | Letter Spacing |
|---------|------|--------|-------------|----------------|
| H1 | 32px | 700 | 1.2 | -0.02em |
| H2 | 24px | 600 | 1.3 | -0.01em |
| H3 | 20px | 600 | 1.4 | 0 |
| H4 | 18px | 500 | 1.4 | 0 |
| Body Large | 18px | 400 | 1.5 | 0 |
| Body | 16px | 400 | 1.5 | 0 |
| Body Small | 14px | 400 | 1.5 | 0.01em |
| Caption | 12px | 500 | 1.4 | 0.02em |
| Button | 16px | 600 | 1 | 0.01em |

## Touch Target Sizes

- **Minimum**: 44x44px
- **Recommended**: 48x48px
- **Large Buttons**: 56px height
- **Spacing Between**: 16px minimum

## Responsive Breakpoints

- **Tablet Landscape**: 1024px - 1366px (Primary target)
- **Tablet Portrait**: 768px - 1023px
- **Desktop**: 1367px+

## Performance Targets

- **First Contentful Paint**: < 1.5s
- **Time to Interactive**: < 3s
- **Animation Frame Rate**: 60fps
- **Bundle Size**: < 200KB (gzipped)

## Data Models

### Service
```typescript
interface Service {
  id: string;
  plate: string;
  vehicle: Vehicle;
  client: Client;
  status: 'in_progress' | 'waiting_pickup' | 'completed';
  entryDate: Date;
  exitDate?: Date;
  observations: string;
  inspectionPhotos: InspectionPhoto[];
  signature?: string; // base64
}
```

### Vehicle
```typescript
interface Vehicle {
  plate: string;
  brand: string;
  model: string;
  year: number;
  color: string;
  mileage: number;
}
```

### Client
```typescript
interface Client {
  name: string;
  phone: string;
  email?: string;
  document?: string;
}
```

### InspectionPhoto
```typescript
interface InspectionPhoto {
  id: string;
  region: string;
  url: string;
  timestamp: Date;
}
```

## State Management (Zustand)

### Service Store
```typescript
interface ServiceStore {
  services: Service[];
  selectedService: Service | null;
  addService: (service: Service) => void;
  updateService: (id: string, updates: Partial<Service>) => void;
  selectService: (id: string | null) => void;
  completeService: (id: string, signature: string) => void;
}
```

### Settings Store
```typescript
interface SettingsStore {
  workshopName: string;
  workshopLogo?: string;
  reportHeader: string;
  showLogoInReport: boolean;
  requireSignature: boolean;
  updateSettings: (settings: Partial<Settings>) => void;
}
```

## Local Storage Schema

```typescript
// Key: autocheck-services
interface StoredData {
  services: Service[];
  settings: Settings;
  lastBackup: string;
}
```

## Keyboard Shortcuts

| Shortcut | Action |
|----------|--------|
| Ctrl + N | New Check-in |
| Ctrl + H | Go to History |
| Ctrl + S | Go to Settings |
| Esc | Cancel/Close modal |
| Enter | Confirm action |

## Error Handling

- **Form Validation**: Real-time with Zod
- **API Errors**: Toast notifications
- **Camera Errors**: Fallback to file upload
- **Storage Errors**: Alert with retry option

## Security Considerations

- **Signature Data**: Stored as base64, encrypted at rest
- **Client Data**: PII masked in logs
- **Local Storage**: XSS protection, data validation
