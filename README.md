# Anime Movies (Kotlin + Compose)

A minimal Android app that lists anime movies and shows details, built with MVVM, Kotlin, Jetpack Compose, and Hilt.

## Features

* Movies list screen (25 items from Jikan API).
* Movie detail screen with title, poster, and description.
* Loading & error states.
* Stateless UI driven by ViewModel state.

## Tech Stack

* Kotlin, Coroutines/Flow
* Jetpack Compose (UI)
* Hilt (DI)
* Retrofit + JSON converter (network)
* (Optional) Coil for image loading

## API

* Base: `https://api.jikan.moe/v4`
* Endpoint used: `/anime?type=movie&page=1&limit=25`
* Key fields: `mal_id`, `title`, `images.jpg.large_image_url`, `synopsis`

## Architecture

* **UI (Compose):** `MoviesListScreen`, `MovieDetailScreen`
* **Presentation:** `MoviesViewModel`, `MovieDetailViewModel`
* **Data:** Repository + Retrofit service
* **DI:** Hilt modules provide service and repository
* **State Flow:** UI ← ViewModel (Flow/State) ← Repository ← API

## Project Structure

```
data/          (api service, dto, repository)
di/            (hilt modules)
model/         (ui/domain models, mappers)
ui/movies/     (list screen, viewmodel)
ui/detail/     (detail screen, viewmodel)
util/          (result/state wrappers)
```
## Screens

<img src="https://github.com/ysyogendra22/Movies/blob/main/Screenshot_20251029_121019.png" width="200"> <img src="https://github.com/ysyogendra22/Movies/blob/main/Screenshot_20251029_121041.png" width="200">




## Setup & Run

1. Open in latest Android Studio and sync.
2. Ensure internet permission is declared.
3. Build and run on device/emulator (no API key required).

## Testing

* Unit tests for repositories and viewmodels.
* Compose UI tests for list and detail flows.

## Acknowledgements

* Data provided by the Jikan API.
