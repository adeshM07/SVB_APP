# API Integration Readiness Plan

## Goal
Prepare the app for reliable backend integration using a clean, scalable structure (`presentation -> domain -> data`) while minimizing risky refactors.

## Current State (Observed)
- `presentation` layer is dominant (screens + navigation).
- Only one ViewModel (`ClockInFlowViewModel`) exists and it is demo/local-state driven.
- `di/AppModule.kt` exists but is currently empty.
- No clear `data` layer (API service, DTOs, repository implementations) is wired yet.
- Breakdown report flow is currently UI-only submit behavior.

## Target Project Structure
Use feature-first packages with layered internals so each feature can integrate independently.

```text
app/src/main/java/com/svb/fieldops/
  core/
    common/
      Resource.kt
      UiEvent.kt
      DispatchersProvider.kt
    network/
      ApiConstants.kt
      NetworkResult.kt
      SafeApiCall.kt
      auth/
        AuthInterceptor.kt
      error/
        ApiErrorParser.kt
  di/
    NetworkModule.kt
    RepositoryModule.kt
    DispatcherModule.kt
    AppModule.kt
  data/
    remote/
      api/
        BreakdownApiService.kt
        FuelApiService.kt
      dto/
        request/
          ReportBreakdownRequestDto.kt
        response/
          ReportBreakdownResponseDto.kt
      mapper/
        BreakdownDtoMapper.kt
    repository/
      BreakdownRepositoryImpl.kt
      FuelRepositoryImpl.kt
  domain/
    model/
      breakdown/
        BreakdownSeverity.kt
        BreakdownType.kt
        BreakdownReport.kt
    repository/
      BreakdownRepository.kt
    usecase/
      breakdown/
        ReportBreakdownUseCase.kt
        ValidateBreakdownInputUseCase.kt
  presentation/
    screens/
      breakdowns/
        SupervisorReportBreakdownScreen.kt
    viewmodel/
      breakdowns/
        ReportBreakdownViewModel.kt
      state/
        ReportBreakdownUiState.kt
        ReportBreakdownUiEvent.kt
```

## Conventions to Keep
- Keep navigation in `presentation/navigation`.
- Keep role-based screen gating (`supportsXxxScreen`) as-is.
- Use Hilt for constructor injection and module bindings.
- Keep business rules in `domain/usecase`, not in Composables.
- Do not delete existing mock/demo data while backend is pending.

## Mock-First Compatibility Rule
Until backend is production-ready, all API integration work must preserve current mock-backed behavior.

### Non-Negotiables
- Keep existing mock data and fallback flows intact.
- Introduce abstraction first (interfaces/use cases), then add API path in parallel.
- Default runtime path remains mock unless explicitly switched.
- Avoid direct Retrofit usage in UI so mock and API can be swapped without screen rewrites.

### Toggle Strategy (Planned)
- Support two data sources per feature: `Mock` and `Api`.
- Select source through DI/config (for example `BuildConfig` flag or Hilt qualifier).
- Start with `Mock` as default; enable `Api` selectively per environment when backend is ready.
- Ensure both sources return the same domain models to keep UI stable.

## Phased Implementation Plan

### Phase 0: API Contract Finalization (No code refactor yet)
- Confirm endpoint(s), HTTP method, headers, auth scheme, timeout and retry expectations.
- Confirm mandatory fields for breakdown submission:
  - role/user identity
  - machine/trip/job identifiers
  - severity
  - breakdown type
  - notes/comment rules
- Confirm success payload and error payload schema.
- Output: agreed request/response schema doc.

### Phase 1: Core Network Foundation
- Add Retrofit/OkHttp setup in `di/NetworkModule.kt`.
- Add auth interceptor/token provider scaffold (even if token is temporary).
- Add `SafeApiCall` + unified error parsing.
- Keep this phase additive only; do not remove or replace existing mock providers.
- Output: reusable network stack and standardized error handling.

### Phase 2: Domain Contracts
- Define domain models and enums for breakdown.
- Add `BreakdownRepository` interface.
- Add use cases:
  - `ValidateBreakdownInputUseCase`
  - `ReportBreakdownUseCase`
- Output: stable business layer independent of transport.

### Phase 3: Data Layer for Breakdown
- Create `BreakdownApiService`.
- Add request/response DTOs and mappers.
- Implement both `BreakdownApiRepositoryImpl` and `BreakdownMockRepositoryImpl` (or equivalent dual data source).
- Bind repository via selector/provider so active source can be switched without changing UI code.
- Output: mock-safe data path with API-ready parallel implementation.

### Phase 4: Presentation Wiring (Breakdown Feature)
- Add `ReportBreakdownViewModel` with:
  - input state
  - submit loading flag
  - validation error state
  - one-shot success/failure events
- Update `SupervisorReportBreakdownScreen` to:
  - read state from ViewModel
  - call submit action
  - show loading and API errors
  - navigate only on success
- Output: first end-to-end integrated feature.

### Phase 5: Hardening and Reuse
- Add unit tests for validation and use case behavior.
- Add repository tests for DTO mapping and error conversion.
- Add UI tests for submit states (idle/loading/success/error).
- Extract reusable patterns for other modules (fuel, trips, reports).
- Output: repeatable API integration template for remaining screens.

## First Feature Scope (Recommended)
Start with **Supervisor Report Breakdown** only, then replicate pattern.

### Why this first
- Isolated screen with clear submit action.
- Limited dependencies compared to multi-step flows.
- High value as a blueprint for other API-backed forms.

## Deliverables Checklist
- [ ] Network module with Retrofit/OkHttp
- [ ] Auth interceptor/token source abstraction
- [ ] Standardized API error mapping
- [ ] Breakdown domain models + repository contract
- [ ] Breakdown DTOs + mapper + API interface
- [ ] Dual repository/data source path (mock + api) with configurable selection
- [ ] Report breakdown ViewModel + UI state/events
- [ ] Screen wired to ViewModel submit flow
- [ ] Unit tests (use case + mapper)
- [ ] Basic UI state tests

## Risks and Mitigations
- **Risk:** backend schema changes during implementation  
  **Mitigation:** lock API contract before Phase 2.
- **Risk:** inconsistent error UX across screens  
  **Mitigation:** centralize error mapping and UI event model in `core`.
- **Risk:** large refactor slows feature delivery  
  **Mitigation:** migrate one feature at a time (strangler pattern).
- **Risk:** mock data accidentally removed before backend readiness  
  **Mitigation:** enforce mock-first compatibility rule and dual-source repository strategy.

## Definition of Done (for Integration-Ready)
- Breakdown report can be submitted to real backend from UI.
- Loading, validation, and API error states are visible and stable.
- Navigation happens only after confirmed backend success.
- Layer boundaries are respected (`presentation` does not call Retrofit directly).
- Pattern is reusable for at least one more feature with minimal copy changes.

