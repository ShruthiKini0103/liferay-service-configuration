FROM jenkins/jenkins:2.423-jdk11

ENV JAVA_OPTS "-Djenkins.install.runSetupWizard=false -Dpermissive-script-security.enabled=true"
ENV CASC_JENKINS_CONFIG var/jenkins_home/casc.yaml

RUN jenkins-plugin-cli \
    --plugins \
    ant:497.v94e7d9fffa_b_9 \
    antisamy-markup-formatter:162.v0e6ec0fcfcf6 \
    authorize-project:1.7.1 \
    basic-branch-build-strategies:81.v05e333931c7d \
    blueocean:1.27.9 \
    branch-api:2.1128.v717130d4f816 \
    build-discarder:139.v05696a_7fe240 \
    build-timeout:1.31 \
    cloudbees-folder:6.858.v898218f3609d \
    configuration-as-code:1775.v810dc950b_514 \
    credentials-binding:642.v737c34dea_6c2 \
    email-ext:2.102 \
    git:5.2.1 \
    github-branch-source:1752.vc201a_0235d80 \
    github-pr-comment-build:96.v9ff13b69dd66 \
    gradle:2.9 \
    http_request:1.18 \
    jaxb:2.3.9-1 \
    job-dsl:1.87 \
    ldap:711.vb_d1a_491714dc \
    mailer:463.vedf8358e006b_ \
    matrix-auth:3.2.1 \
    pam-auth:1.10 \
    permissive-script-security:0.7 \
    pipeline-github-lib:42.v0739460cda_c4 \
    pipeline-utility-steps:2.16.0 \
    pipeline-stage-view:2.34 \
    oic-auth:2.6 \
    strict-crumb-issuer:2.1.1 \
    ssh-slaves:2.916.vd17b_43357ce4 \
    timestamper:1.26 \
    workflow-aggregator:596.v8c21c963d92d \
    ws-cleanup:0.45 \
    slack:684.v833089650554

COPY --chown=jenkins:jenkins casc.yaml /var/jenkins_home/casc.yaml

ENV JENKINS_REF_DIR="/usr/share/jenkins/ref"
ENV PATH="$PATH:$JENKINS_HOME/.local/bin"

RUN echo "---->" $JENKINS_HOME

RUN set -x && \
      curl https://cdn.liferay.cloud/lcp/stable/latest/install.sh -fsSL | bash && \
      lcp version && \
      mkdir -p $JENKINS_REF_DIR/.local/bin && \
      mv $JENKINS_HOME/.local/bin/lcp $JENKINS_REF_DIR/.local/bin/lcp.override