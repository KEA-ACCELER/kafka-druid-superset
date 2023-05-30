FROM apache/superset:latest

ARG SUPERSET_SECRET_KEY

RUN superset fab create-admin \
              --username admin \
              --firstname Superset \
              --lastname Admin \
              --email admin@superset.com \
              --password admin
# other commands ...
RUN superset db upgrade
# RUN superset load_examples
RUN superset init

RUN pip install pydruid