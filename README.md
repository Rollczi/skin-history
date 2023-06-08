# LiteSkinHistory
#### by Rollczi for vCrispiDev
Repozytorium zawiera 3 moduły:
- skin-history-core - główny moduł zawierający logikę dla pluginu Bukkit i Velocity
- skin-history-bukkit - moduł zawierający implementację dla serwerów Bukkit (Gui, komendy, pobieranie skinów)
- skin-history-velocity - moduł zawierający implementację dla serwerów Velocity (Zapisywanie skinów)

#### Konfiguracja
Konfiguracja bazy danych znajduje się w pliku `database.yml` w folderze `plugins/LiteSkinHistory` (Bukkit) lub `plugins/liteskinhistory` (Velocity)
Konfiguracja pluginu bukkit znajduje się w pliku `config.yml` w folderze `plugins/LiteSkinHistory`

#### Komendy
- `/skin-history` - Wyświetla GUI z historią skinów

#### Testy jednostkowe i integracyjne
Testy jednostkowe i integracyjne znajdują się w module `skin-history-core` w folderze `test`
Testy integracyjne wymagają uruchomienia dockera. Polecam zainstalować https://www.docker.com/products/docker-desktop/
Testują one połączenie i operacje na bazie danych MySQL.
