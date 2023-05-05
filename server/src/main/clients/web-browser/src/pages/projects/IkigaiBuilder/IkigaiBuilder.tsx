import './IkigaiBuilder.scss';
import {Input, Layout, Modal} from 'antd';
import {Coordinate, Ikigai} from '../../../Ikigai/Ikigai';
import {useMeasure} from '@react-hookz/web';
import {ChangeEvent, useEffect, useState, useRef, forwardRef} from 'react';
import {createService, pl_types, project_management} from '../../../protos';
import {getCurrentUser} from '../../../utils/authentication';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';
import {CloseOutlined, PlusOutlined} from '@ant-design/icons';
import IEks = pl_types.IEks;
import IXqCompetency = pl_types.IXqCompetency;
import ProjectManagementService = project_management.ProjectManagementService;
import {useNavigate} from 'react-router';

const {Content} = Layout;

const FreeTextInput = forwardRef<
  HTMLDivElement,
  {
    id: string;
    shortTitle: string;
    hint?: string;
    inputPlaceholder: string;
    values: string[];
    onValuesUpdated: (values: string[]) => void;
    maxNumberOfValues: number;
  }
>((props, ref) => {
  const [modalOpen, setModalOpen] = useState(false);
  const [editingValues, setEditingValues] = useState<string[]>([]);

  function onClick() {
    const valuesCopy = [...props.values];
    if (valuesCopy.length === 0) {
      valuesCopy.push('');
    }
    setEditingValues(valuesCopy);
    setModalOpen(true);
  }

  function onOk() {
    setModalOpen(false);
    props.onValuesUpdated(editingValues.filter(value => value.length > 0));
  }

  function onCancel() {
    setModalOpen(false);
  }

  function setEditingValue(index: number, value: string) {
    const valuesCopy = [...editingValues];
    valuesCopy[index] = value;
    setEditingValues(valuesCopy);
  }

  function removeEditingValue(index: number) {
    const valuesCopy = [...editingValues];
    valuesCopy.splice(index, 1);
    if (valuesCopy.length === 0) {
      valuesCopy.push('');
    }
    setEditingValues(valuesCopy);
  }

  function addEditingValue() {
    setEditingValues([...editingValues, '']);
  }

  return (
    <>
      <div id={props.id} className="panel" ref={ref} onClick={onClick}>
        <div className="title">{props.shortTitle}</div>
        <div className="body">
          {props.values.length > 0 ? (
            <>
              <div>
                <div style={{textAlign: 'left', width: 'fit-content'}}>
                  {props.values.map((value, index) => (
                    <span key={index} style={{whiteSpace: 'nowrap'}}>
                      - {value}
                      <br />
                    </span>
                  ))}
                </div>
              </div>
            </>
          ) : (
            <div className="hint">
              {props.hint != null ? props.hint : 'Select to modify.'}
            </div>
          )}
        </div>
      </div>
      <Modal
        title={props.shortTitle}
        open={modalOpen}
        closable={true}
        onOk={onOk}
        onCancel={onCancel}
      >
        {editingValues.map((value, index) => (
          <>
            <div key={index} className="line-item">
              <Input
                placeholder={props.inputPlaceholder}
                maxLength={255}
                onChange={(e: ChangeEvent<HTMLInputElement>) => {
                  setEditingValue(index, e.target.value);
                }}
                value={value}
              />
              <CloseOutlined
                style={{
                  visibility: editingValues.length > 1 ? 'visible' : 'hidden',
                }}
                onClick={() => removeEditingValue(index)}
              />
            </div>
          </>
        ))}
        <PlusOutlined
          className="add-line-item"
          onClick={() => addEditingValue()}
          style={{
            display:
              editingValues.length < props.maxNumberOfValues
                ? 'inline-block'
                : 'none',
          }}
        />
      </Modal>
    </>
  );
});

const DropdownSelectInput = forwardRef<
  HTMLDivElement,
  {
    id: string;
    shortTitle: string;
    hint?: string;
    inputPlaceholder: string;
    options: Map<number, {}>;
    values: number[];
    onValuesUpdated: (values: number[]) => void;
    maxNumberOfValues: number;
    optionToLabel: (option: {} | undefined) => string;
  }
>((props, ref) => {
  const [modalOpen, setModalOpen] = useState(false);
  const [editingValues, setEditingValues] = useState<number[]>([]);

  function onClick() {
    const valuesCopy = [...props.values];
    if (valuesCopy.length === 0) {
      valuesCopy.push(-1);
    }
    setEditingValues(valuesCopy);
    setModalOpen(true);
  }

  function onOk() {
    setModalOpen(false);
    props.onValuesUpdated(editingValues.filter(value => value >= 0));
  }

  function onCancel() {
    setModalOpen(false);
  }

  function setEditingValue(index: number, value: number) {
    const valuesCopy = [...editingValues];
    valuesCopy[index] = value;
    setEditingValues(valuesCopy);
  }

  function removeEditingValue(index: number) {
    const valuesCopy = [...editingValues];
    valuesCopy.splice(index, 1);
    if (valuesCopy.length === 0) {
      valuesCopy.push(-1);
    }
    setEditingValues(valuesCopy);
  }

  function addEditingValue() {
    setEditingValues([...editingValues, -1]);
  }

  return (
    <>
      <div id={props.id} className="panel" ref={ref} onClick={onClick}>
        <div className="title">{props.shortTitle}</div>
        <div className="body">
          {props.values.length > 0 ? (
            <>
              <div>
                <div style={{textAlign: 'left'}}>
                  {props.values.map((value, index) => (
                    <span key={index} style={{whiteSpace: 'nowrap'}}>
                      - {props.optionToLabel(props.options.get(value))}
                      <br />
                    </span>
                  ))}
                </div>
              </div>
            </>
          ) : (
            <div className="hint">
              {props.hint != null ? props.hint : 'Select to modify.'}
            </div>
          )}
        </div>
      </div>
      <Modal
        title={props.shortTitle}
        open={modalOpen}
        closable={true}
        onOk={onOk}
        onCancel={onCancel}
      >
        {editingValues.map((value, index) => (
          <>
            <div key={index} className="line-item">
              <select
                key={index}
                value={value}
                onChange={(e: ChangeEvent<HTMLSelectElement>) => {
                  setEditingValue(index, Number.parseInt(e.target.value));
                }}
              >
                <option key={-1} value={-1}>
                  {props.inputPlaceholder}
                </option>
                {[...props.options.keys()].map(key => (
                  <option key={key} value={key}>
                    {props.optionToLabel(props.options.get(key))}
                  </option>
                ))}
              </select>
              <CloseOutlined
                style={{
                  visibility: editingValues.length > 1 ? 'visible' : 'hidden',
                }}
                onClick={() => removeEditingValue(index)}
              />
            </div>
          </>
        ))}
        <PlusOutlined
          className="add-line-item"
          onClick={() => addEditingValue()}
          style={{
            display:
              editingValues.length < props.maxNumberOfValues
                ? 'inline-block'
                : 'none',
          }}
        />
      </Modal>
    </>
  );
});

export function IkigaiBuilder() {
  const user = getCurrentUser();
  if (user == null) {
    return <></>;
  }

  const [ikigaiCenterPosition, setIkigaiCenterPosition] =
    useState<Coordinate | null>(null);
  const [ikigaiDistanceToCategoryCenter, setIkigaiDistanceToCategoryCenter] =
    useState(0);
  const [ikigaiCategoryDiameter, setIkigaiCategoryDiameter] = useState(0);

  const [ikigaiContainerMeasure, ikigaiContainerMeasureRef] =
    useMeasure<HTMLDivElement>();
  const [useIkigaiResizeTimeout, setUseIkigaiResizeTimeout] = useState(true);
  const [ikigaiResizeTimeoutId, setIkigaiResizeTimeoutId] = useState<
    NodeJS.Timeout | undefined
  >(undefined);

  const [processing, setProcessing] = useState(false);

  // Resize and reposition the Ikigai diagram to be consistent with the window.
  function updateIkigaiPosition() {
    if (
      ikigaiContainerMeasureRef.current != null &&
      ikigaiContainerMeasure != null
    ) {
      setIkigaiCenterPosition({
        x:
          ikigaiContainerMeasureRef.current.offsetLeft +
          ikigaiContainerMeasure.width / 2,
        y:
          ikigaiContainerMeasureRef.current.offsetTop +
          ikigaiContainerMeasure.height / 2,
      });
      setIkigaiCategoryDiameter(
        Math.min(ikigaiContainerMeasure.width, ikigaiContainerMeasure.height) /
          2
      );
      setIkigaiDistanceToCategoryCenter(
        (Math.min(ikigaiContainerMeasure.width, ikigaiContainerMeasure.height) /
          2) *
          0.45
      );
    }
    setUseIkigaiResizeTimeout(false);
  }

  useEffect(() => {
    // TODO: This is a terrible solution. Revisit.
    if (useIkigaiResizeTimeout) {
      if (ikigaiResizeTimeoutId != null) {
        clearTimeout(ikigaiResizeTimeoutId!);
      }
      setIkigaiResizeTimeoutId(setTimeout(updateIkigaiPosition, 500));
    } else {
      updateIkigaiPosition();
    }
  }, [ikigaiContainerMeasureRef, ikigaiContainerMeasure]);

  // function getLovesRelatedSuggestions() {
  //   const partialTextOpenAiPromptService = createService(
  //     PartialTextOpenAiPromptService,
  //     'PartialTextOpenAiPromptService'
  //   );
  //   setLovesGetRelatedSuggestionsEnabled(false);
  //   partialTextOpenAiPromptService
  //     .getSuggestions({
  //       partialText: modalLovesValue,
  //       prompt: Prompt.SUGGEST_THINGS_YOU_LOVE,
  //       userXId: user!.userXId!,
  //     })
  //     .then(response => setLovesSuggestions(response.suggestions))
  //     .catch(() => setLovesSuggestions([]))
  //     .finally(() => setLovesGetRelatedSuggestionsEnabled(true));
  // }

  function onSpinClick() {
    setProcessing(true);

    const service = createService(
      ProjectManagementService,
      'ProjectManagementService'
    );

    service
      .generateProjects({
        userXId: user?.userXId,
        interests: studentInterests,
        career: careers,
        eks: eks.map(id => eksOptions.get(id)!),
        xqCompentencies: xqCompetencies.map(id => xqCompetencyOptions.get(id)!),
      })
      .finally(() => useNavigate()('/projects/my-projects'));
  }

  const [studentInterests, setStudentInterests] = useState<string[]>([]);
  const studentInterestsRef = useRef<HTMLDivElement>(null);

  const [eks, setEks] = useState<number[]>([]);
  const [eksOptions, setEksOptions] = useState(new Map<number, IEks>());
  const eksRef = useRef<HTMLDivElement>(null);

  const [xqCompetencies, setXqCompetencies] = useState<number[]>([]);
  const [xqCompetencyOptions, setXqCompetencyOptions] = useState(
    new Map<number, IXqCompetency>()
  );
  const xqCompetencyRef = useRef<HTMLDivElement>(null);

  const [careers, setCareers] = useState<string[]>([]);
  const careersRef = useRef<HTMLDivElement>(null);

  // const [motivations, setMotivations] = useState<string[]>([]);
  // const motivationsRef = useRef<HTMLDivElement>(null);

  // Initialize the data.
  useEffect(() => {
    const service = createService(
      ProjectManagementService,
      'ProjectManagementService'
    );

    service
      .getEks({userXId: user.userXId})
      .then(resp =>
        setEksOptions(new Map(resp.eks.map(eks => [eks.id!, eks])))
      );
    service
      .getXqCompetencies({userXId: user.userXId})
      .then(resp =>
        setXqCompetencyOptions(
          new Map(resp.xqCompentencies.map(xqc => [xqc.id!, xqc]))
        )
      );
  }, []);

  return (
    <>
      <DefaultPage title="Ikigai Project Builder">
        <Layout style={{height: '100%'}}>
          <Content style={{borderRight: '#F0781F solid 1px', padding: '0.5em'}}>
            <div
              style={{width: '100%', height: '100%'}}
              ref={ikigaiContainerMeasureRef}
            >
              <Ikigai
                id="ikigai-builder"
                centerPosition={ikigaiCenterPosition}
                categoryDiameter={ikigaiCategoryDiameter}
                distanceToCategoryCenter={ikigaiDistanceToCategoryCenter}
                radians={0}
                enabled={!processing}
                processing={processing}
                categoryElementIds={[
                  careersRef.current?.id,
                  xqCompetencyRef.current?.id,
                  eksRef.current?.id,
                  studentInterestsRef.current?.id,
                  // motivationsRef
                ]}
                showSpinButton={
                  careers.length > 0 &&
                  studentInterests.length > 0 &&
                  eks.length > 0 &&
                  xqCompetencies.length > 0
                }
                onSpinClick={onSpinClick}
                radiansOffset={0}
              >
                <FreeTextInput
                  id="studentInterests"
                  ref={studentInterestsRef}
                  shortTitle="Student Interests"
                  hint="Click to add student interests."
                  inputPlaceholder="Enter a Student Interest"
                  values={studentInterests}
                  maxNumberOfValues={4}
                  onValuesUpdated={setStudentInterests}
                />
                <DropdownSelectInput
                  id="eks"
                  ref={eksRef}
                  shortTitle="Knowledge and Skills"
                  hint="Click to add desired knowledge and skills."
                  inputPlaceholder="Select a Knowledge and Skills"
                  options={eksOptions}
                  values={eks}
                  maxNumberOfValues={4}
                  onValuesUpdated={setEks}
                  optionToLabel={value => (value as IEks).name!}
                />
                <DropdownSelectInput
                  id="xqCompetency"
                  ref={xqCompetencyRef}
                  shortTitle="XQ Competency"
                  hint="Click to add desired XQ Competency."
                  inputPlaceholder="Select an XQ Competency"
                  options={xqCompetencyOptions}
                  values={xqCompetencies}
                  maxNumberOfValues={4}
                  onValuesUpdated={setXqCompetencies}
                  optionToLabel={value => (value as IXqCompetency).name!}
                />
                <FreeTextInput
                  id="careers"
                  ref={careersRef}
                  shortTitle="Career Interest"
                  hint="Click to set a career."
                  inputPlaceholder="Enter a Career Name"
                  values={careers}
                  maxNumberOfValues={4}
                  onValuesUpdated={setCareers}
                />
                {/*<FreeTextInput*/}
                {/*  id="motivations"*/}
                {/*  ref={motivationsRef}*/}
                {/*  shortTitle="Motivations"*/}
                {/*  hint="Click to add a motivation."*/}
                {/*  inputPlaceholder="Motivation"*/}
                {/*  values={motivations}*/}
                {/*  maxNumberOfValues={4}*/}
                {/*  onValuesUpdated={setMotivations}*/}
                {/*/>*/}
              </Ikigai>
            </div>
          </Content>
        </Layout>
        <Modal
          centered
          width="50%"
          open={processing}
          closable={false}
          footer={null}
        >
          <div
            style={{
              textAlign: 'center',
              width: '100%',
            }}
          >
            Finding great projects! Please wait. This can take a few minutes.
          </div>
        </Modal>
      </DefaultPage>
    </>
  );
}
