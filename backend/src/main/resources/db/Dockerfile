FROM cgr.dev/chainguard/git
RUN mkdir /home/git/.ssh
ARG SSH_PRIVATE_KEY
ARG SSH_PUBLIC_KEY
ARG SSH_PASSPHRASE
RUN printf %s\\n "$SSH_PRIVATE_KEY" > /home/git/.ssh/id_rsa
RUN printf %s\\n "$SSH_PUBLIC_KEY" > /home/git/.ssh/id_rsa.pub
RUN chmod 600 /home/git/.ssh/id_rsa && chmod 600 /home/git/.ssh/id_rsa.pub
RUN chown -R git:git /home/git/.ssh
RUN eval $(ssh-agent -s) && echo "$SSH_PASSPHRASE" | xargs
# read -s SSH_PASSPHRASE && echo "$SSH_PASSPHRASE" | xargs podman build --build-arg SSH_PRIVATE_KEY="$(cat "$HOME"/.ssh/id_ed25519)" --build-arg SSH_PUBLIC_KEY="$(cat "$HOME"/.ssh/id_ed25519.pub)" --build-arg SSH_PASSPHRASE="$SSH_PASSPHRASE" -t ctbs .
# podman build --build-arg SSH_PRIVATE_KEY="$(cat "$HOME"/.ssh/id_ed25519)" --build-arg SSH_PUBLIC_KEY="$(cat "$HOME"/.ssh/id_ed25519.pub)" -t ctbs .
# podman run --rm -it --entrypoint /bin/sh ctbs
# eval $(ssh-agent -s) && ssh-add /home/git/.ssh/id_rsa
# git clone git@github.com:isaacsimstudy/CSIT314-TeamHotDog-Project.git
