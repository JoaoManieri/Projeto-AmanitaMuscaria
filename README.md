# Projeto AmanitaMuscaria

## Tratamento de Erros (guia rápido)
- **Injeção**: `ErrorHandler` está no Koin (`errorModule`). Injete no `ViewModel`: `class XViewModel(..., private val errorHandler: ErrorHandler)`.
- **Captura**: Em blocos `try/catch`, use `errorHandler.handle(e, "Mensagem opcional")`. Exceções comuns (timeout, rede, unauthorized) são mapeadas automaticamente pelo `ErrorMapper`.
- **Manual**: Para cenários sem exceção, use `errorHandler.handleType(ErrorType.ValidationError(...), message, action)`.
- **UI (Fragment/Activity)**: Crie `ErrorUIController(errorHandler)` e chame `observeErrors(viewLifecycleOwner, anchorView) { errorResult -> ... }`. O callback recebe `ErrorResult` para executar `retry/logout/go_home/come_back`.
- **Ações padrão**: Timeout/Network/Database/FileSave → `RETRY`; Unauthorized → `LOGOUT`; Validation → `NONE`; Unexpected → `GO_HOME`; CameraPermission → `COME_BACK`.
- **Compose (opcional)**: `remember { ErrorUIController(errorHandler) }` e colete `errors` em `LaunchedEffect`, mostrando `Snackbar` ou `AlertDialog` conforme `errorResult.action`.
- **Navegação**: No callback, trate `RETRY` (reexecutar operação), `LOGOUT` (limpar sessão e navegar), `GO_HOME`/`COME_BACK` (NavController), `NONE` (ignorar).
