#!/bin/sh -eu
# This is posix, so we send to dev/null to avoid a warning
printf "Enter passphrase for SSH key: " && read -r ssh_passphrase > /dev/null
SSH_PASSPHRASE="$(printf "%s\n%s" "#!/bin/sh" "echo $ssh_passphrase")"
podman rmi -f ctbs
podman build \
  --build-arg SSH_PRIVATE_KEY="$(cat "$HOME"/.ssh/id_ed25519)" \
  --build-arg SSH_PUBLIC_KEY="$(cat "$HOME"/.ssh/id_ed25519.pub)" \
  --build-arg SSH_PASSPHRASE="$SSH_PASSPHRASE" \
  -t ctbs .
podman run --rm -it --entrypoint /bin/sh ctbs